/**
 *
 * PassCode - Password Generator
 * Copyright (c) 2014, Sandeep Gupta
 * 
 * http://sangupta.com/projects/passcode
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 		http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package com.sangupta.passcode;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.commons.lang3.StringUtils;

import com.sangupta.jerry.util.AssertUtils;

/**
 * Class that actually computes the password based on
 * a given set of options.
 * 
 * @author sangupta
 *
 */

public class PassCode {
	
	private static final int NUM_INTERATIONS = 8;
	
	private static final double LOG_2 = Math.log(2);
	
	private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
	
	private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	private static final String ALPHA = LOWER + UPPER;
	
	private static final String NUMBER = "0123456789";
	
	private static final String ALPHANUM = ALPHA + NUMBER;
	
	private static final String SPACE = " ";
	
	private static final String DASH = "-_";
	
	private static final String SYMBOL = "!\"#$%&\'()*+,./:;<=>?@[\\]^{|}~" + DASH;
	
	private static final String ALL = ALPHANUM + SPACE + SYMBOL;
	
	private final Config config;
	
	private String allowed = ALL;
	
	private final List<String> required = new ArrayList<String>();
	
	public PassCode(Config config) {
		this.config = config;
		
		initialize();
		
//		System.out.println("required: " + this.required);
//		System.out.println("allowed: " + this.allowed);
	}

	private void initialize() {
		updateAllowedAndRequired(LOWER, config.lower);
		updateAllowedAndRequired(UPPER, config.upper);
		updateAllowedAndRequired(NUMBER, config.number);
		updateAllowedAndRequired(SPACE, config.space);
		updateAllowedAndRequired(DASH, config.dash);
		updateAllowedAndRequired(SYMBOL, config.symbol);
		
		int delta = config.length - this.required.size();
		if(delta > 0) {
			while(delta > 0) {
				this.required.add(this.allowed);
				delta--;
			}
		}
	}
	
	private void updateAllowedAndRequired(final String charset, int number) {
		if(number < 0) {
			return;
		}
		
		if(number == 0) {
			this.allowed = StringUtils.replaceChars(this.allowed, charset, "");
		} else {
			while(number > 0) {
				this.required.add(charset);
				number--;
			}
		}
	}
	
	private byte[] hash(String password, String salt, int entropy) {
		// generate the hash
		try {
			PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), NUM_INTERATIONS, entropy + 12);
	        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
	        return skf.generateSecret(spec).getEncoded();
		} catch (GeneralSecurityException e) {
			System.out.println("Something went wrong!");
			System.exit(0);
		}
		
		return null;
	}

	/**
	 * Generate the unique password for the given master password and the salt (the site's
	 * key).
	 * 
	 * @param masterPassword
	 * @param saltOrSiteKey
	 * @return
	 */
	public String generate(String masterPassword, String saltOrSiteKey) {
		if(this.required.size() > this.config.length) {
			throw new IllegalStateException("Length too small to fit all required characters");
		}
		
		if(this.allowed.length() == 0) {
			throw new IllegalStateException("No characters available to create a password");
		}
		
		// check for uuid append
		if(AssertUtils.isNotEmpty(this.config.uuid)) {
			saltOrSiteKey = saltOrSiteKey + this.config.uuid;
		}
		
		// get hash
		final double entropy = this.getEntropy();
//		System.out.println("Entropy: " + entropy);
		
		byte[] hash = this.hash(masterPassword, saltOrSiteKey, 2 * (int) entropy);
//		System.out.println("Hex: " + com.sangupta.jerry.util.StringUtils.asHex(hash));
		
		// convert to hash stream
		HashStream stream = new HashStream(hash);
		
		// start encoding the password
		// refer http://checkmyworking.com/2012/06/converting-a-stream-of-binary-digits-to-a-stream-of-base-n-digits/
		// on the process
		// adapted for https://github.com/jcoglan/vault
		
		String result = "";
//		int iter = 1;
		int previous = 0;
		
		
		while(result.length() < this.config.length) {
			int index = stream.generate(this.required.size(), 2, false);
			String charset = this.required.remove(index);
//			if(!result.isEmpty()) {
//				previous = result.charAt(result.length() - 1);
//			} else {
//				previous = 0;
//			}
			
			
			int i = this.config.repeat - 1;
			boolean same = (previous > 0) && (i >= 0);
			
//			System.out.println("iter: " + (iter++) + ", index:" + index + ", previous: " + Character.toString((char) previous) + ", i: " + i + ", same: " + same + ", charset: " + charset);
			
			while(same && (i-- >= 0)) {
				same = same && isSameChar(result, result.length() + i - this.config.repeat, previous);
			}
			
			if(same) {
				charset = StringUtils.remove(this.allowed, charset);
			}
			
			index = stream.generate(charset.length(), 2, false);
			result += charset.charAt(index);
			previous = charset.charAt(index);
		}
		
		return result;
	}
	
	/**
	 * Check if the character at the given position index in the given string
	 * is same as that provided. Takes care of all boundary conditions.
	 * 
	 * @param string
	 * @param index
	 * @param character
	 * @return
	 */
	private boolean isSameChar(String string, int index, int character) {
		if(string == null || string.isEmpty()) {
			return false;
		}
		
		if(index < 0) {
			return false;
		}
		
		final int length = string.length();
		if(index >= length) {
			return false;
		}
		
		return string.charAt(index) == (char) character;
	}

	/**
	 * Compute the entropy for this password generation.
	 * 
	 */
	private double getEntropy() {
		double entropy = 0;
		for(int i = 0; i < this.required.size(); i++) {
			entropy += Math.ceil(Math.log(i+1) / LOG_2);
		    entropy += Math.ceil(Math.log(this.required.get(i).length()) / LOG_2);	
		}
		
//		System.out.println("entropy: " + entropy);
		return entropy;
	}
}
