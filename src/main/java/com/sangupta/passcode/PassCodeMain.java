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

import com.sangupta.jerry.util.AssertUtils;
import com.sangupta.jerry.util.ConsoleUtils;

import io.airlift.command.ParseOptionMissingException;
import io.airlift.command.SingleCommand;


/**
 * Command line tool for Password generation. Reads parameters from
 * the command line and then appropriately calls the {@link PassCode}
 * tool.
 * 
 * @author sangupta
 * 
 */
public class PassCodeMain {
	
	/**
	 * UUID being used by the vault code base
	 */
	private static final String VAULT_UUID = "e87eb0f4-34cb-46b9-93ad-766c5ab063e7";
	
	public static void main(String[] args) {
		Config config = SingleCommand.singleCommand(Config.class).parse(args);
		
		if(config.helpOption.showHelpIfRequested()) {
			return;
		}
		
		try {
			if(AssertUtils.isEmpty(config.siteKeyWord)) {
				if(config.secure) {
					config.siteKeyWord = ConsoleUtils.readPassword("Site keyword: ", true);
				} else {
					config.siteKeyWord = ConsoleUtils.readLine("Site keyword: ", true);
				}
				
				if(AssertUtils.isEmpty(config.siteKeyWord)) {
					System.out.println("No site keyword selected, nothing to do... exiting!");
					return;
				}
			}
			
			String password = ConsoleUtils.readPassword("Passphrase: ", true);
			if(AssertUtils.isEmpty(password)) {
				System.out.println("No pass phrase selected, nothing to generate... exiting!");
				return;
			}
			
			if(AssertUtils.isEmpty(config.uuid)) {
				config.uuid = VAULT_UUID;
			}
			
 			PassCode passCode = new PassCode(config);
			System.out.println("Password: " + passCode.generate(password, config.siteKeyWord));
		} catch(ParseOptionMissingException e) {
			System.out.println("HTTP Toolbox: " + e.getMessage());
			System.out.println("Use -h for usage instructions.");
//			Help.help(command);
		}
	}
	
}
