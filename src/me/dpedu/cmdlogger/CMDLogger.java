package me.dpedu.cmdlogger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class CMDLogger extends JavaPlugin implements Listener {
	
	public void onEnable() {
		// Create the "CMDLogger" dir
		checkDataDirExists();
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	@EventHandler
	public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent pcpe) {
		// When a command is fired, create a string summarizing the event
		Player p = pcpe.getPlayer();
		Location loc = p.getLocation();
		String line = getDateString()+" ["+p.getName()+"("+p.getAddress().getAddress().toString().substring(1)+")@"+p.getWorld().getName()+":"+(int)loc.getX()+","+(int)loc.getY()+","+(int)loc.getZ()+"] "+pcpe.getMessage();
		File dest;
		// Fetch the log File we need
		if(p.isOp()) {
			dest = getOpsLog();
		} else {
			dest = getNormalLog();
		}
		// And write it
		appendLineToLog(line, dest);
	}
	
	public String getDateString() {
		// Returns a string of the current date/time for use in the log.
		Date myDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(myDate);
	}
	
	public void appendLineToLog(String line, File log) {
		// Appends the text given to the file given
		FileWriter f;
		try {
			f = new FileWriter(log, true);
			f.write(line+"\n");
			f.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public File getNormalLog() {
		// Return the File for the log for player commands
		return new File( this.getDataFolder() + "/commands.log");
	}
	public File getOpsLog() {
		// Return the File for the log for op commands
		return new File( this.getDataFolder() + "/opcommands.log");
	}
	public void checkDataDirExists() {
		// Create the CMDLogger dir if it doesn't exist.
		File dir = new File(""+this.getDataFolder());
		if(!dir.exists()) {
			dir.mkdir();
		}
	}
}
