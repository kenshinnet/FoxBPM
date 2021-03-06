/**
 * Copyright 1996-2014 FoxBPM ORG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * @author kenshin
 * @author ych
 */
package org.foxbpm.engine.impl.interceptor;

import org.foxbpm.engine.exception.FoxBPMException;
import org.foxbpm.engine.impl.Context;

/**
 * foxbpm command执行器
 * @author ych
 *
 */
public class CommandInvoker extends CommandInterceptor {

	 
	public <T> T execute(CommandConfig config,Command<T> command) {
		return command.execute(Context.getCommandContext());
	}
	
	 
	public void setNext(CommandInterceptor commandInterceptor) {
		throw new FoxBPMException("commandInvoke必须是最后一个拦截器");
	}

}
