/*
This file is part of the OdinMS Maple Story Server
Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc>
Matthias Butz <matze@odinms.de>
Jan Christian Meyer <vimes@odinms.de>

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as
published by the Free Software Foundation version 3 as published by
the Free Software Foundation. You may not use, modify or distribute
this program under any other version of the GNU Affero General Public
License.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package scripting.item;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.HashMap;
import java.util.Map;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import tools.FilePrinter;
import tools.MaplePacketCreator;
import client.MapleClient;

public class ItemScriptManager {

	private static ItemScriptManager instance = new ItemScriptManager();
	private final Map<String, Invocable> scripts = new HashMap<>();
	private final ScriptEngineFactory sef;

	private ItemScriptManager() {
		final ScriptEngineManager sem = new ScriptEngineManager();
		this.sef = sem.getEngineByName("javascript").getFactory();
	}

	public static ItemScriptManager getInstance() {
		return instance;
	}

	public boolean scriptExists(String scriptName) {
		final File scriptFile = new File("scripts/item/" + scriptName + ".js");
		return scriptFile.exists();
	}

	public void getItemScript(MapleClient c, String scriptName) {
		if (this.scripts.containsKey(scriptName)) {
			try {
				this.scripts.get(scriptName).invokeFunction("start",
						new ItemScriptMethods(c));
			} catch (ScriptException | NoSuchMethodException ex) {
				FilePrinter.printError(FilePrinter.ITEM + scriptName + ".txt",
						ex);
			}
			return;
		}
		final File scriptFile = new File("scripts/item/" + scriptName + ".js");
		if (!scriptFile.exists()) {
			c.announce(MaplePacketCreator.enableActions());
			return;
		}
		FileReader fr = null;
		final ScriptEngine portal = this.sef.getScriptEngine();
		try {
			fr = new FileReader(scriptFile);
			final CompiledScript compiled = ((Compilable) portal).compile(fr);
			compiled.eval();

			final Invocable script = ((Invocable) portal);
			this.scripts.put(scriptName, script);
			script.invokeFunction("start", new ItemScriptMethods(c));
		} catch (final UndeclaredThrowableException | ScriptException ute) {
			FilePrinter.printError(FilePrinter.ITEM + scriptName + ".txt", ute);
		} catch (final Exception e) {
			FilePrinter.printError(FilePrinter.ITEM + scriptName + ".txt", e);
		} finally {
			if (fr != null) {
				try {
					fr.close();
				} catch (final IOException e) {
				}
			}
		}
	}
}