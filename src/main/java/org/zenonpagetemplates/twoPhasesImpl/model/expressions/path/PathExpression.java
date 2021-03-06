package org.zenonpagetemplates.twoPhasesImpl.model.expressions.path;

import java.util.ArrayList;
import java.util.List;

import org.zenonpagetemplates.common.ExpressionTokenizer;
import org.zenonpagetemplates.common.exceptions.EvaluationException;
import org.zenonpagetemplates.common.exceptions.ExpressionSyntaxException;
import org.zenonpagetemplates.common.exceptions.NoSuchPathException;
import org.zenonpagetemplates.common.scripting.EvaluationHelper;
import org.zenonpagetemplates.twoPhasesImpl.TwoPhasesPageTemplate;
import org.zenonpagetemplates.twoPhasesImpl.model.expressions.ZPTExpression;
import org.zenonpagetemplates.twoPhasesImpl.model.expressions.ZPTExpressionImpl;
import org.zenonpagetemplates.twoPhasesImpl.model.expressions.StringExpression;
import org.zenonpagetemplates.twoPhasesImpl.model.expressions.path.literals.BooleanLiteralExpression;
import org.zenonpagetemplates.twoPhasesImpl.model.expressions.path.literals.DoubleLiteralExpression;
import org.zenonpagetemplates.twoPhasesImpl.model.expressions.path.literals.FloatLiteralExpression;
import org.zenonpagetemplates.twoPhasesImpl.model.expressions.path.literals.IntegerLiteralExpression;
import org.zenonpagetemplates.twoPhasesImpl.model.expressions.path.literals.LongLiteralExpression;
import org.zenonpagetemplates.twoPhasesImpl.model.expressions.path.literals.StringLiteralExpression;

/**
 * <p>
 *   Implements an expression using a list of PathExpressionItem: it evaluates
 *   the first one; if it is not null return the value, otherwise continue
 *   evaluating the next PathExpressionItem.
 * </p>
 * 
 * 
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
 *
 * @author <a href="mailto:chris@christophermrossi.com">Chris Rossi</a>
 * @author <a href="mailto:david.javapagetemplates@gmail.com">David Cana</a>
 * @version $Revision: 1.2 $
 */
public class PathExpression extends ZPTExpressionImpl implements ZPTExpression {

	private static final long serialVersionUID = -2480164535532949474L;
	
	private List<ZPTExpression> expressions = new ArrayList<ZPTExpression>();
	
	
	public PathExpression(){}

	public PathExpression( String stringExpression, Path path ){
		super( stringExpression );
		
		this.expressions.add( path );
	}


	@Override
	public Object evaluate( EvaluationHelper evaluationHelper ) throws EvaluationException {
		
		NoSuchPathException exception = null;
		
		for ( ZPTExpression zptExpression: this.expressions ){
			try {
				Object result = zptExpression.evaluate( evaluationHelper );
				if ( result != null ){
					return result;
				}
			} catch ( NoSuchPathException e ) {
				exception = e;
			}
		}
        
		if ( exception != null ) {
            throw exception;
        }
        
		return null;
	}
	
	static public Object evaluate( String pathExpression, EvaluationHelper evaluationHelper ) 
		throws ExpressionSyntaxException, EvaluationException {
		return generate( pathExpression ).evaluate( evaluationHelper );
	}
	
	static public ZPTExpression generate( String exp ) throws ExpressionSyntaxException {
		
        String expression = exp;
        
        // Blank expression evaluates to blank string
        if ( expression.trim().length() == 0 ) {
            return new StringLiteralExpression( StringExpression.VOID_STRING );
        }

        ExpressionTokenizer segments = new ExpressionTokenizer( 
        		expression, 
        		TwoPhasesPageTemplate.PATH_DELIMITER );
        
        if ( segments.countTokens() == 1 ) {
            return generateFromPathSegment( expression );
        }

        PathExpression result = new PathExpression();
        while ( segments.hasMoreTokens() ) {
            String segment = segments.nextToken().trim();
            result.expressions.add(
            		new GeneralPathExpressionItem( segment ) );
        }
        
        return result;
	}
	
    private static final ZPTExpression generateFromPathSegment( String expression ) 
    	throws ExpressionSyntaxException {
    	
        // Blank expression evaluates to blank string
        if ( expression.length() == 0 ) {
        	return new Path(
        			new StringLiteralExpression( StringExpression.VOID_STRING ) );
        }
     
        // Evaluate first token
        ExpressionTokenizer tokenizer = new ExpressionTokenizer( expression, TwoPhasesPageTemplate.PATH_SEGMENT_DELIMITER );
        String token = tokenizer.nextToken().trim();
        FirstPathToken firstPathToken = generateFirstPathToken( token );
        
        // If the path has only one token, return a single expression
        if ( ! tokenizer.hasMoreTokens() ){
        	return new Path( firstPathToken );
        }
        
        // Otherwise, generate a Path for the PathExpression
        Path path = new Path( firstPathToken );
        
        // Traverse the portalObjectPath
        while ( tokenizer.hasMoreTokens() ) {
            token = tokenizer.nextToken().trim();
            NextPathToken nextPathToken = generateNextPathToken( token, path, expression );
            
            if ( nextPathToken instanceof ArrayExpression ){
            	path = new Path( ( FirstPathToken ) nextPathToken );
            } else {
            	path.addToken( nextPathToken );
            }
        }

        return path;
    }
    

    private static final FirstPathToken generateFirstPathToken( String tok ) throws ExpressionSyntaxException {
    	
        String token = new String( tok );
        
        // Separate identifier from any array accessors
        String arrayAccessor = null;
        int bracket = ArrayExpression.findArrayAccessor( token );
        //if ( bracket != -1 ) {
        if ( bracket > 0 ) { // Not list expressions
            arrayAccessor = token.substring( bracket ).trim();
            token = token.substring( 0, bracket ).trim();
        }

        // First token must come from scope or be a literal

        // First see if it's a string literal
        FirstPathToken result = StringLiteralExpression.generate( token );
        if ( result == null ) {

	        // If it's not, try to see if it's a number
	        result = generateNumericLiteralExpression( token );
	        if ( result == null ) {
	        	
	            // Maybe it's a boolean literal
	            result = BooleanLiteralExpression.generate( token );
	            if ( result == null ) {
	            	
	            	// A list?
	            	result = ListExpression.generate( token );
	            	if ( result == null ) {	
	            	
		                // It could be a class, for a static method call
		                result = StaticCallExpression.generate( token );
		                if ( result == null ) {
		                    
		                	// Or it could be an actual reference to a class object
		                    result = ClassExpression.generate( token );
		                    if ( result == null ) {
		                        
		                    	// Must be an object in scope
		                        result = VarNameExpression.generate( token );
		                    }
		                }
	                }
	            }
	        }
        }
        
        // If it is an array
        if ( arrayAccessor != null && result instanceof ZPTExpression) {
        	result = ArrayExpression.generate( token, ( ZPTExpression ) result, arrayAccessor);
        }
        
        return result;
    }
    
	static private FirstPathToken generateNumericLiteralExpression( String token ) {
		
		FirstPathToken result = LongLiteralExpression.generate( token );
        if ( result != null ) {
            return result;
        }
        
		result = DoubleLiteralExpression.generate( token );
        if ( result != null ) {
            return result;
        }
        
		result = FloatLiteralExpression.generate( token );
        if ( result != null ) {
            return result;
        }
        
		return IntegerLiteralExpression.generate( token );
	}

    private static final NextPathToken generateNextPathToken( String tok, Path path, String stringExpression )
        throws ExpressionSyntaxException {
    	
        String token = new String( tok );
        
        // Separate identifier from any array accessors
        String arrayAccessor = null;
        int bracket = ArrayExpression.findArrayAccessor( token );
        if ( bracket != -1 ) {
            arrayAccessor = token.substring( bracket ).trim();
            token = token.substring( 0, bracket ).trim();
        }

        // Element is a method or property of parent
        NextPathToken result = null;
        
        // Test for indirection
        result = Indirection.generate( token );

        if ( result == null ){
	        // A method call?
	        result = MethodCallExpression.generate( token );
	        
	        if (result == null){
	            // A property
	            result = PropertyExpression.generate( token );
	        }
        }
        
        // If it is an array
        if ( arrayAccessor != null ) {
        	path.addToken( result );
        	result = ArrayExpression.generate( 
        			token, 
        			new PathExpression( stringExpression, path ), 
        			arrayAccessor );
        }
        
        return result;
    }
    
    public static Object evaluateNextPathToken( String tok, Object parent, EvaluationHelper evaluationHelper )
            throws ExpressionSyntaxException, EvaluationException {
    	
        String token = new String( tok );
        
        // Separate identifier from any array accessors
        String arrayAccessor = null;
        int bracket = ArrayExpression.findArrayAccessor( token );
        if ( bracket != -1 ) {
            arrayAccessor = token.substring( bracket ).trim();
            token = token.substring( 0, bracket ).trim();
        }

        // Element is a method or property of parent
        Object result = null;
        
        // Test for indirection
        Indirection indirection = Indirection.generate( token );
        if ( indirection != null ){
        	result = indirection.evaluate( parent, evaluationHelper );
        }

        if ( result == null ){
	        // A method call?
        	MethodCallExpression methodCallExpression = MethodCallExpression.generate( token );
        	if ( methodCallExpression != null ){
        		result = methodCallExpression.evaluate( parent, evaluationHelper );
        	}
	        
	        if ( result == null ){
	            // A property
	        	PropertyExpression propertyExpression = PropertyExpression.generate( token );
	        	if ( propertyExpression != null ){
	        		result = propertyExpression.evaluate( parent, evaluationHelper );
	        	}
	        }
        }
        
        // If it is an array
        if ( arrayAccessor != null && result instanceof ZPTExpression) {
        	ArrayExpression arrayExpression = ArrayExpression.generate( 
        			token, ( ZPTExpression ) result, arrayAccessor );
        	return arrayExpression.evaluate( parent, evaluationHelper );
        }
        
        // It could be a script
        ScriptExpression scriptExpression = ScriptExpression.generate( result );
        if ( scriptExpression != null ){
        	return scriptExpression.evaluate( parent, evaluationHelper );
        }
        
        return result;
    }

    @Override
    public String toString(){
    	return this.stringExpression;
    }
    
}
