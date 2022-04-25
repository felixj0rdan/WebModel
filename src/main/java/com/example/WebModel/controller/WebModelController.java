package com.example.WebModel.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import smile.data.DataFrame;
import smile.io.Read;

@RestController
public class WebModelController {
	
	// global declaration of a TestSmile object 
	private static TestSmile model = new TestSmile();
	
	// we get the training files' directory form application.properties
	@Value("${file.upload-dir-train}")
	String TRAIN_FILE_DIR;

	
	@PostMapping("/train-model") // set the api route
	public ResponseEntity<Object> trainModel(@RequestParam("File") MultipartFile file) throws IOException, URISyntaxException {

		// first we have to receive and and store the csv training set file
		
		String path = TRAIN_FILE_DIR+file.getOriginalFilename(); // set the path where the file is going to get saved
		File myFile = new File(path); // create a new file in that location
		myFile.createNewFile();
		
		// now we write all the data from the file we received to the file we created
		FileOutputStream fos = new FileOutputStream(myFile);
		fos.write(file.getBytes());
		fos.close();
		
		// now we use the file we created to train the model
		DataFrame train = Read.csv(path); // load the data as a dataframe
		
		// training the model
		model.trainModel(train);
		
		// to display the model metrics
		model.modelMetrics();
		
		System.out.println(model.forest);
	
		// we return a response based on the model we trained
		return new ResponseEntity<Object>(model.metrics, HttpStatus.OK);
	}
	
	
	// we get the testing files' directory form the application.properties
	@Value("${file.upload-dir-test}")
	String TEST_FILE_DIR;
	@PostMapping("/test-model") // set the api route
	public ResponseEntity<Object> testModel(@RequestParam("File") MultipartFile file) throws IOException, URISyntaxException {
		
		
		// first we have to receive and and store the csv training set file
		
		String path = TEST_FILE_DIR+file.getOriginalFilename(); // set the path where the file is going to get saved
		File myFile = new File(path); // create a new file in that location
		myFile.createNewFile();
		FileOutputStream fos = new FileOutputStream(myFile);
		fos.write(file.getBytes());
		fos.close();
		
		// now we use the file we created to test the model
		DataFrame test = Read.csv(path); // load the data as a dataframe

		// we store the results of the test in a result array
		int result[] = model.testModel(test);
		model.modelMetrics();
	
		
		return new ResponseEntity<Object>(result, HttpStatus.OK);
	}
	
	// we get the testing files' directory form the application.properties
		@Value("${file.upload-dir-verify}")
		String VER_FILE_DIR;
		@PostMapping("/verify") // set the api route
		public ResponseEntity<Object> verifyPredictions(@RequestParam("File") MultipartFile file) throws IOException, URISyntaxException {
			
			
			// first we have to receive and and store the verification csv file
			
			String path = VER_FILE_DIR+file.getOriginalFilename(); // set the path where the file is going to get saved
			File myFile = new File(path); // create a new file in that location
			myFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(myFile);
			fos.write(file.getBytes());
			fos.close();
			
			// passing the file path to the function testAccuracy() 
			double accuracy = model.testAccuracy(path);

			model.modelMetrics();
		
			// return thr accuracy parameter that was calculated
			return new ResponseEntity<Object>(accuracy, HttpStatus.OK);
		}
	
}
