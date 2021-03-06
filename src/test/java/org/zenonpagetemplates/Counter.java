package org.zenonpagetemplates;

import java.util.ArrayList;
import java.util.List;

/**
 *  Zenon Page Templates
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 3 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *  
 *  @author <a href="mailto:rossi+wzcommon@webslingerZ.com">Chris Rossi</a>
 *  @author <a href="mailto:david.javapagetemplates@gmail.com">David Cana</a>
 *  @version $Revision: 1.11 $
 */

public class Counter {

	private int c = 0;
	
	Counter(){}
	
	public int getValue(){
		return ++this.c;
	}
	
	public List<Integer> getList(){
		
		List<Integer> result = new ArrayList<Integer>();
		
		for ( int i = 0; i < this.c; ++i ){
			result.add( i );
		}
		
		return result;
	}
	
	public ATestClass getTestInstance(){
		return new ATestClass( "a string with the counter: " + ++this.c );
	}
	
	
	public class ATestClass {
		private String value;
		
		ATestClass( String value ){
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
	}
}
