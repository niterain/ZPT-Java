package org.zenonpagetemplates.onePhaseImpl;

import java.io.IOException;
import java.util.Map;
import java.util.Stack;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;
import org.zenonpagetemplates.common.exceptions.EvaluationException;
import org.zenonpagetemplates.common.exceptions.PageTemplateException;
import org.zenonpagetemplates.common.scripting.EvaluationHelper;

/**
 * <p>
 *   Interface to implement by slot classes.
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
 * @version $Revision: 1.5 $
 */
public interface Slot {

	void process( ContentHandler contentHandler, 
                  LexicalHandler lexicalHandler, 
                  EvaluationHelper evaluationHelper,
                  Stack <Map<String, Slot>>slotStack )
        throws SAXException, PageTemplateException, IOException, EvaluationException;
}
