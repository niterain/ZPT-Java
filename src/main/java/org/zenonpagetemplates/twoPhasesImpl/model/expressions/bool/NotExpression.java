package org.zenonpagetemplates.twoPhasesImpl.model.expressions.bool;

import org.zenonpagetemplates.common.exceptions.EvaluationException;
import org.zenonpagetemplates.common.exceptions.ExpressionSyntaxException;
import org.zenonpagetemplates.common.scripting.EvaluationHelper;
import org.zenonpagetemplates.twoPhasesImpl.TwoPhasesPageTemplate;
import org.zenonpagetemplates.twoPhasesImpl.model.expressions.EvaluableToBoolean;
import org.zenonpagetemplates.twoPhasesImpl.model.expressions.ExpressionUtils;
import org.zenonpagetemplates.twoPhasesImpl.model.expressions.ZPTExpression;
import org.zenonpagetemplates.twoPhasesImpl.model.expressions.ZPTExpressionImpl;

/**
 * <p>
 *   Defines a NOT expression.
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
 * @author <a href="mailto:david.javapagetemplates@gmail.com">David Cana</a>
 * @version $Revision: 1.0 $
 */
public class NotExpression extends ZPTExpressionImpl implements EvaluableToBoolean {

	private static final long serialVersionUID = 4107795468596398923L;
	
	private ZPTExpression expression;
	
	
	public NotExpression(){}
	
	public NotExpression( String stringExpression, ZPTExpression expression ){
		super( stringExpression );
		
		this.expression = expression;
	}

	public ZPTExpression getExpression() {
		return this.expression;
	}

	public void setExpression( ZPTExpression expression ) {
		this.expression = expression;
	}

	static public NotExpression generate( String expression ) throws ExpressionSyntaxException {
		
		return new NotExpression(
				expression, 
				ExpressionUtils.generate( 
						expression.substring( TwoPhasesPageTemplate.EXPR_NOT.length() ).trim() ) );
	}
	
	@Override
	public Object evaluate( EvaluationHelper evaluationHelper ) throws EvaluationException {
		return evaluateToBoolean( evaluationHelper );
	}

	@Override
	public Boolean evaluateToBoolean( EvaluationHelper evaluationHelper )
			throws EvaluationException {
		return ! ExpressionUtils.evaluateToBoolean( this.expression, evaluationHelper );
	}
	
	static public Object evaluate( String exp, EvaluationHelper evaluationHelper ) 
			throws EvaluationException, ExpressionSyntaxException {
		return generate( exp ).evaluateToBoolean( evaluationHelper ); 
	}
}
