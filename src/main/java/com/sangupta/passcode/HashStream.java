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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * A simple stream of bits that is obtained by generating
 * a binary-representation of a hash/string. Callee's may
 * then consume this continuous stream of bits as a stream
 * reading any number of arbitrary bits and converting them
 * to any required base.
 * 
 * @author sangupta
 *
 */
public class HashStream {
	
	private final String bits;
	
	private final Map<Integer, List<Integer>> bases = new HashMap<Integer, List<Integer>>();
	
	public HashStream(byte[] hash) {
		this.bits = asBinary(hash);
//		System.out.println("Binary: " + bits);
		
		this.bases.put(2, map(this.bits, 2));
	}
	
	private List<Integer> map(String bits, int base) {
		if(base == 2) {
			List<Integer> list = new ArrayList<Integer>();
			for(int index = 0; index < bits.length(); index++) {
				list.add(Integer.parseInt(String.valueOf(bits.charAt(index)), 2));
			}
			
			return list;
		}
		
		throw new RuntimeException("not implemented");
	}

	public int generate(int n, int base, boolean inner) {
		int value = n;
		int k = (int) Math.ceil(Math.log(n) / Math.log(base));
		int r = (int) Math.pow(base, k) - n;
		
		while(value >= n) {
			List<Integer> chunk = this.shift(base, k);
			
			if(chunk == null) {
				return inner ? n : 0;
			}
			
			value = this.evaluate(chunk, base);
			if(value >= n) {
				if(r == 1d) {
					continue;
				}
				
				this.push(r, value - n);
				value = this.generate(n, r, true);
			}
		}
		
		return value;
	}
	
	private int evaluate(List<Integer> chunk, int base) {
		int sum = 0;
		int i = chunk.size();
		
		while((i--) > 0) {
			sum += chunk.get(i) * Math.pow(base, chunk.size() - (i + 1));
		}
		
		return sum;
	}
	
	private void push(int base, int value) {
		List<Integer> list = this.bases.get(base);
		if(list == null) {
			list = new ArrayList<Integer>();
			this.bases.put(base, list);
		}
		
		list.add(value);
	}
	
	private List<Integer> shift(int base, int k) {
		List<Integer> list = this.bases.get(base);
		if(list == null || list.size() < k) {
			return null;
		}
		
		return splice(list, 0, k);
	}
	
	private static String asBinary(byte[] hash) {
		String binary = "";
		for(byte b : hash) {
			int unsigned = b & 0xFF;
			String bin = Integer.toBinaryString(unsigned);
			bin = StringUtils.leftPad(bin, 8, '0');
			binary = binary + bin;
		}
		
		return binary;
	}

	private static List<Integer> splice(List<Integer> list, int index, int howMany) {
		List<Integer> newList = new ArrayList<Integer>();
		final int end = Math.min(index + howMany, list.size());
		if(index > end) {
			return newList;
		}
		
		for(int i = index; i < end; i++) {
			newList.add(list.remove(index));
		}
		
		return newList;
	}
	
}
