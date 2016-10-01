package org.zenonpagetemplates.twoPhasesImpl.model.expressions.path;

import org.zenonpagetemplates.common.exceptions.EvaluationException;
import org.zenonpagetemplates.common.exceptions.ExpressionSyntaxException;
import org.zenonpagetemplates.common.scripting.EvaluationHelper;
import org.zenonpagetemplates.twoPhasesImpl.model.expressions.ExpressionUtils;
import org.zenonpagetemplates.twoPhasesImpl.model.expressions.ZPTExpression;

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
 *
 * @author <a href="mailto:david.javapagetemplates@gmail.com">David Cana</a>
 * @version $Revision: 1.0 $
 */
public class GeneralPathExpressionItem implements ZPTExpression {

	private static final long serialVersionUID = -1840080964134073908L;
	
	private ZPTExpression zptExpression;
	
	public GeneralPathExpressionItem(){}
	public GeneralPathExpressionItem( String stringExpression ) throws ExpressionSyntaxException {
		this.zptExpression = ExpressionUtils.generate( stringExpression );
	}
	public GeneralPathExpressionItem( ZPTExpression zptExpression ){
		this.zptExpression = zptExpression;
	}

	
	public ZPTExpression getZptExpression() {
		return zptExpression;
	}

	public void setZptExpression( ZPTExpression zptExpression ) {
		this.zptExpression = zptExpression;
	}
	
	@Override
	public Object evaluate( EvaluationHelper evaluationHelper ) throws EvaluationException {
		return this.zptExpression.evaluate( evaluationHelper );
	}

	@Override
	public String getStringExpression() {
		return this.zptExpression.getStringExpression();
	}
}
