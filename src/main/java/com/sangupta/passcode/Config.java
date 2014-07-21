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

/**
 * Holds configuration on the options provided to the
 * {@link PassCodeMain} command line tool to generate
 * the passwords.
 * 
 * @author sangupta
 *
 */
public class Config {
	
	/**
	 * The exact length of the password
	 */
	public int length = 10;
	
	/**
	 * The maximum times a character that can be repeated
	 * in the password
	 */
	public int repeat = 5;
	
	/**
	 * Number of upper-case alphabets required. <code>0</code> indicates none
	 * and a negative value says any number of them, including at-least <code>one</code>
	 */
	public int upper = -1;
	
	/**
	 * Number of lower-case alphabets required. <code>0</code> indicates none
	 * and a negative value says any number of them, including at-least <code>one</code>
	 */
	public int lower = -1;
	
	/**
	 * Number of numerals required. <code>0</code> indicates none
	 * and a negative value says any number of them, including at-least <code>one</code>
	 */
	public int number = -1;
	
	/**
	 * Number of dash required (hyphen or underscore). <code>0</code> indicates none
	 * and a negative value says any number of them, including at-least <code>one</code>
	 */
	public int dash = 0;
	
	/**
	 * Number of whitespace (pure space) required. <code>0</code> indicates none
	 * and a negative value says any number of them, including at-least <code>one</code>
	 */
	public int space = 0;
	
	/**
	 * Number of special symbols required. <code>0</code> indicates none
	 * and a negative value says any number of them, including at-least <code>one</code>
	 */
	public int symbol = 0;

}
