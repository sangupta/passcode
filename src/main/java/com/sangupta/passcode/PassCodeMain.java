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
	
	public static void main(String[] args) {
		Config config = SingleCommand.singleCommand(Config.class).parse(args);
		
		if(config.helpOption.showHelpIfRequested()) {
			return;
		}
		
		try {
			PassCode passCode = new PassCode(config);
			System.out.println("Password: " + passCode.generate("test", "google"));
		} catch(ParseOptionMissingException e) {
			System.out.println("HTTP Toolbox: " + e.getMessage());
			System.out.println("Use -h for usage instructions.");
//			Help.help(command);
		}
	}
	
}
