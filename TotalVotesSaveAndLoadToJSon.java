package com.math.tests;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

/**
 * Best and simple Production ready utility to save/load (read/write) data
 * from/to JSON file using the GSon lib.
 * https://github.com/google/gson
 * @author Matheus Ferreira - Rune-Server: https://www.rune-server.ee/members/sagacity/
 */

public class TotalVotesSaveAndLoadingToJSon {

	private static String file_location = "data/saved_constants/total_votes.json";
	private static Gson gson = new Gson();

	// Class with one field
	// - Data
	private static class Data {
		private int voteForExample;

		public int getVoteForExample() {
			return voteForExample;
		}

		public void setVoteForExample(int voteForExample) {
			this.voteForExample = voteForExample;
		}
	}

	// Main Method - to show the basics usages of the snippet in action
	public static void main(String[] args) {
		Data dataToBeSaved = new Data();
		dataToBeSaved.setVoteForExample(Integer.valueOf(readFromFile());
    
    // Setting the voteForExample integer to 3 so we can retrieve later
    dataToBeSaved.setVoteForExample(3);
    
    // Examples of usage  
    //Will read the saved data only (wont print as we aren't sytem.out.printlning it)
    //Should retrieve a value of 3
    readFromFile();
    
		// Save data to file
		writeToFile(gson.toJson(dataToBeSaved));
    
		// Here we are retrieving data from file and printing on the console
		System.out.println(readFromFile());
	}

	public static void incrementTotalVotes (int votes) {
		Data dataToBeSaved = new Data();
		dataToBeSaved.setVoteForExample(Integer.valueOf(readFromFile()) + votes);
		// Save data to file
		writeToFile(gson.toJson(dataToBeSaved));
	}
	
	public static void setTotalVotesForExample(int votes) {
		Data dataToBeSaved = new Data();
		dataToBeSaved.setVote(votes);
		// Save data to GSON file
		writeToFile(gson.toJson(dataToBeSaved));
	}

  // Take total votes (or any other persistent data you want to get)
	public static String getTotalVotesForExample() {
		return readFromFile();
	}

	// Save to file Utility
	private static void writeToFile(String myData) {
		File file = new File(file_location);
		if (!file.exists()) {
			try {
				File directory = new File(file.getParent());
				if (!directory.exists()) {
					directory.mkdirs();
				}
				file.createNewFile();
			} catch (IOException e) {
				logger("Excepton Occured: " + e.toString());
			}
		}

		try {
			FileWriter writter;
			writter = new FileWriter(file.getAbsoluteFile(), false);

			// Writes text to a character-output stream
			BufferedWriter bufferWriter = new BufferedWriter(writter);

			bufferWriter.write(myData.toString());
			bufferWriter.flush();
			bufferWriter.close();

			logger("Data succesfully saved at file location: " + file_location + " Data: " + myData + "\n");
		} catch (IOException e) {
			logger("Error while saving data to file " + e.toString());
		}
	}

	// Read From File
	public static String readFromFile() {
		File file = new File(file_location);
		if (!file.exists())
			logger("File doesn't exist on that directory");

		InputStreamReader isReader;
		try {
			isReader = new InputStreamReader(new FileInputStream(file), "UTF-8");
			JsonReader myReader = new JsonReader(isReader);
			Data dataToBeReaded = gson.fromJson(myReader, Data.class);
			Integer votes = dataToBeReaded.getVoteForExample();
			return votes.toString();
		} catch (Exception e) {
			logger("error load cache from file " + e.toString());
		}

		logger("\n Data successfully loaded from the " + file_location+ "file");
		return "0";
	}

	private static void logger(String string) {
		System.out.println(string);
	}

}
