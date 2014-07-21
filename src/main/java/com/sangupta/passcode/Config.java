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

import javax.inject.Inject;

import io.airlift.command.Command;
import io.airlift.command.HelpOption;
import io.airlift.command.Option;

/**
 * Holds configuration on the options provided to the
 * {@link PassCodeMain} command line tool to generate
 * the passwords.
 * 
 * @author sangupta
 *
 */
@Command(name = "passcode", description = "Strong password generator")
public class Config {
	
	@Inject
    public HelpOption helpOption;
	
	/**
	 * The exact length of the password
	 */
	@Option(name = {"-n", "--length"}, description = "Required length of the password")
	public int length = 10;
	
	/**
	 * The maximum times a character that can be repeated
	 * in the password
	 */
	@Option(name = {"-r", "--repeat"}, description = "Max times a character can repeat")
	public int repeat = 5;
	
	/**
	 * Number of upper-case alphabets required. <code>0</code> indicates none
	 * and a negative value says any number of them, including at-least <code>one</code>
	 */
	@Option(name = {"-u", "--upper"}, description = "Minimum number of upper case characters required")
	public int upper = -1;
	
	/**
	 * Number of lower-case alphabets required. <code>0</code> indicates none
	 * and a negative value says any number of them, including at-least <code>one</code>
	 */
	@Option(name = {"-l", "--lower"}, description = "Minimum number of lower case characters required")
	public int lower = -1;
	
	/**
	 * Number of numerals required. <code>0</code> indicates none
	 * and a negative value says any number of them, including at-least <code>one</code>
	 */
	@Option(name = {"-x", "--number"}, description = "Minimum number of numerals required")
	public int number = -1;
	
	/**
	 * Number of dash required (hyphen or underscore). <code>0</code> indicates none
	 * and a negative value says any number of them, including at-least <code>one</code>
	 */
	@Option(name = {"-d", "--dash"}, description = "Minimum number of dashes (hyphen, underscore) required")
	public int dash = -1;
	
	/**
	 * Number of whitespace (pure space) required. <code>0</code> indicates none
	 * and a negative value says any number of them, including at-least <code>one</code>
	 */
	@Option(name = {"-b", "--blank"}, description = "Minimum number of blank spaces required")
	public int space = -1;
	
	/**
	 * Number of special symbols required. <code>0</code> indicates none
	 * and a negative value says any number of them, including at-least <code>one</code>
	 */
	@Option(name = {"-s", "--symbol"}, description = "Minimum number of special symbols required")
	public int symbol = -1;

}
