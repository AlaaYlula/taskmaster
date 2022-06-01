
# taskmaster
### Lab 26 
create three Activities   
* Main Activity : has two buttons, Add Task and All Tasks.  
* Add Task Activity : will add the titile and description for the task and submit by click on add button.  
* All Tasks Activity : show the task you added and back button to go back to the Main Activity.  
![home](./screenshots/home.PNG)  
  
### Lab 27
create three Activities   
* Main Activity : has three Tasks buttons, menu for setting, and the username text.  
* Settings Activity : Let the user write his/het user name and submit by click on submit button to save the username.  
* DetailTask Activity : show the task title you pressed and description.  
**Home Page**  
![home](./screenshots/AppTask.PNG)  
**Details Page**  
![detail](./screenshots/detailPage.PNG)  

  
### Lab 28
create three Activities   
* Main Activity : has two Tasks buttons, Add Task and All Tasks, the user name , and list for three tasks.  
* Settings Activity : Let the user write his/het user name and submit by click on submit button to save the username.  
* DetailTask Activity : show the task title you pressed, description, and the state for this task.  
* Add CustomAdapter so we can deal with the RecyclerView.   
**Home Page**  
![home](./screenshots/App1.PNG)  
**Details Page**  
![detail](./screenshots/detailTask.PNG)    
  
### Lab 29
create three Activities   
* Main Activity : has two Tasks buttons, Add Task and All Tasks, the user name , and list for the all tasks.  
* Settings Activity : Let the user write his/het user name and submit by click on submit button to save the username.  
* DetailTask Activity : show the task title you pressed, description, and the state for this task. It has delete button for deleteing this task.  
* Add CustomAdapter so we can deal with the RecyclerView. 
* Add Room Library and use the Database to store the tasks record.    
**Home Page**  
![home](./screenshots/App29.PNG)  
**Details Page**  
![detail](./screenshots/Detail29.PNG)      

### Lab 31
Only added the Espresso Test.[Test](https://github.com/AlaaYlula/taskmaster/blob/main/taskmaster/app/src/androidTest/java/com/example/taskmaster/ExampleInstrumentedTest.java)  
create three Activities   
* Main Activity : has two Tasks buttons, Add Task and All Tasks, the user name , and list for the all tasks.  
* Settings Activity : Let the user write his/het user name and submit by click on submit button to save the username.  
* DetailTask Activity : show the task title you pressed, description, and the state for this task. It has delete button for deleteing this task.  
* Add CustomAdapter so we can deal with the RecyclerView. 
* Add Room Library and use the Database to store the tasks record.    
**Home Page**  
![home](./screenshots/App29.PNG)  
**Details Page**  
![detail](./screenshots/Detail29.PNG)      
  
### Lab 32
Start Work with AWS Amplify.  
* Main Activity : has two Tasks buttons, Add Task and All Tasks, the user name , and list for the all tasks(I think still need sync because it has some delay to render the all tasks).  
* Settings Activity : Let the user write his/het user name and submit by click on submit button to save the username.  
* DetailTask Activity : show the task title you pressed, description, and the state for this task. It has delete button for deleteing this task(Not work now). Still not work correctly, I got the correct task in the logcat but not in the Emulator.   
* Add CustomAdapter so we can deal with the RecyclerView. 
* Add  AWS Amplify and use the DynamoDB to store the tasks record.    
**Home Page**  
![home](./screenshots/App32.PNG)  
**DynamoDB**  
![detail](./screenshots/DynamoDB.PNG)       
  
### Lab 33
Start Work with AWS Amplify.  
* Main Activity : has two Tasks buttons, Add Task and All Tasks, the user name , and list for the all tasks(The List of tasks must be only the ones which belongs to the team).
* Settings Activity : Let the user write his/het user name, chose the team and submit by click on submit button to save the username and the team name.  
* DetailTask Activity : show the task title you pressed, description, and the state for this task. It has delete button for deleteing this task(Not work now).  
* AddTask Activity : add the title, description and the team for this Task.  
* Add CustomAdapter so we can deal with the RecyclerView. 
* Add  AWS Amplify and use the DynamoDB to store the tasks record.    
**Home Page**  
![home](./screenshots/App.PNG)  
**DynamoDB**  
![team](./screenshots/teamDynamo.PNG)  
  
### Lab 34
Build a signed APK for the application.  
Start Work with AWS Amplify.  
* Main Activity : has two Tasks buttons, Add Task and All Tasks, the user name , and list for the all tasks(The List of tasks must be only the ones which belongs to the team).
* Settings Activity : Let the user write his/het user name, chose the team and submit by click on submit button to save the username and the team name.  
* DetailTask Activity : show the task title you pressed, description, and the state for this task. It has delete button for deleteing this task(Not work now).  
* AddTask Activity : add the title, description and the team for this Task.  
* Add CustomAdapter so we can deal with the RecyclerView. 
* Add  AWS Amplify and use the DynamoDB to store the tasks record.    
**Home Page**  
![home](./screenshots/App.PNG)  
**DynamoDB**  
![team](./screenshots/teamDynamo.PNG)  

### Lab 36
Use Cognito for Authentication.
Build a signed APK for the application.  
Start Work with AWS Amplify.  
* Login page: has signup prompt. And create a AlertDialog if error happens.  
* Signup page: has signin prompt. And create a AlertDialog if error happens. redirect to the verification code page when end the signup.   
* Verification code page: Enter the verification code and go to loggin page.  
* Main Activity : has two Tasks buttons, Add Task and All Tasks, the user name , and list for the all tasks(The List of tasks must be only the ones which belongs to the team).
* Settings Activity : Let the user write his/het user name, chose the team and submit by click on submit button to save the username and the team name.  
* DetailTask Activity : show the task title you pressed, description, and the state for this task. It has delete button for deleteing this task(Not work now).  
* AddTask Activity : add the title, description and the team for this Task.  
* Add CustomAdapter so we can deal with the RecyclerView. 
* Add  AWS Amplify and use the DynamoDB to store the tasks record.    
**Home Page**  
![home](./screenshots/main36.PNG)  
**DynamoDB**  
![team](./screenshots/user.PNG)  
**login**  
![team](./screenshots/login.PNG)  
**Signup**  
![team](./screenshots/signup.PNG)  


### Lab 37
Use S3 for store images.  
Use Cognito for Authentication.
Build a signed APK for the application.  
Start Work with AWS Amplify.  
* Login page: has signup prompt. And create a AlertDialog if error happens.  
* Signup page: has signin prompt. And create a AlertDialog if error happens. redirect to the verification code page when end the signup.   
* Verification code page: Enter the verification code and go to loggin page.  
* Main Activity : has two Tasks buttons, Add Task and All Tasks, the user name , and list for the all tasks(The List of tasks must be only the ones which belongs to the team).
* Settings Activity : Let the user write his/het user name, chose the team and submit by click on submit button to save the username and the team name.  
* DetailTask Activity : show the image for this task and the task title you pressed, description, and the state for this task. It has delete button for deleteing this task(Not work now).  
* AddTask Activity : add the title, description, image and the team for this Task.  
* Add CustomAdapter so we can deal with the RecyclerView. 
* Add  AWS Amplify and use the DynamoDB to store the tasks record.    
**Add Task Page**  
![home](./screenshots/Add37.PNG)  
**Detail Page**  
![detial](./screenshots/detail37.PNG)
**S3**  
![team](./screenshots/S3Images.PNG)  

  
### Lab 38
Use Intent Filter to receive image from other applications.  
Use S3 for store images.  
Use Cognito for Authentication.
Build a signed APK for the application.  
Start Work with AWS Amplify.  
* Login page: has signup prompt. And create a AlertDialog if error happens.  
* Signup page: has signin prompt. And create a AlertDialog if error happens. redirect to the verification code page when end the signup.   
* Verification code page: Enter the verification code and go to loggin page.  
* Main Activity : has two Tasks buttons, Add Task and All Tasks, the user name , and list for the all tasks(The List of tasks must be only the ones which belongs to the team).
* Settings Activity : Let the user write his/het user name, chose the team and submit by click on submit button to save the username and the team name.  
* DetailTask Activity : show the image for this task and the task title you pressed, description, and the state for this task. It has delete button for deleteing this task(Not work now).  
* AddTask Activity : add the title, description, image and the team for this Task.  
If the Application receive an image from other application this activity will get the intent, and upload the image to the task.  
* Add CustomAdapter so we can deal with the RecyclerView. 
* Add  AWS Amplify and use the DynamoDB to store the tasks record.   
**Login**  
![login](./screenshots/task38/signin38.PNG)  
**Share from fb**  
![fb](./screenshots/task38/share38.PNG)   
**Add Task Page_receive the Image**  
![home](./screenshots/task38/uploaded38.PNG)    
**Add Task Page_add the task**  
![add](./screenshots/task38/added38.PNG)  
**Detail Page**  
![detial](./screenshots/task38/task38.PNG)  
