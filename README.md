# Parby Social Network

Brought to you by ANT Team

# Screenshots

<img src="https://user-images.githubusercontent.com/22562689/41874011-5187c890-78cf-11e8-93b3-280e85c47310.png" height="800px" width="800px">

## 1. Purpose: 
- This project is to create a community where parents (especially new parents) can post their little angle's images to keep their memories together. 

## 2. Team member: 
- Anh Vu, Nguyen Nguyen, Toan Thanh

## 3. Work division:

#### Anh Vu: Backend developer
  - Server: REST Api with JavaEE, Glassfish 5
  - Database: MySQL
  - Mockup
  
  
#### Nguyen Nguyen: Frontend developer
  - User experience
  - HTML
  - CSS 
  - Javascript
  - Main Debugger
  
#### Toan Thanh: Frontend developer
  - User experience
  - HTML
  - CSS 
  - Javascript

## 4. User groups:

#### User can
- Post Images 
- Like/Comment/Delete Other Images
- See all images of other users (within their permission)

#### Admin can
- Normal Users' functions
- Delete any images (policy-violated contents)

#### 1. Login page:

- User can sign up or login (check for existing username) with password (will be hashed)
- Give user a token to keep track of login state and grant permisson
- Auto redirect to main page if user has already logged in

#### 2. Main page:

- Start with newsfeed to see newest images
- Upload images modal
- Users can like (once per post) and comment on the posts
- Each post (image) has tracks on the number of likes, views and comments
- Auto redirect to login page when the user signs out.

#### Back-end 

## In this project, we imports 2 libraries for authorization purposes. We can add 2 dependencies by adding these lines in your pom.xml (if you are using Maven):

<dependency>
            <groupId>com.auth0</groupId>
            <artifactId>java-jwt</artifactId>
            <version>3.3.0</version>
        </dependency>
<dependency>
            <groupId>org.mindrot</groupId>
            <artifactId>jbcrypt</artifactId>
            <version>0.3m</version>
            <type>jar</type>
        </dependency>
#First library is for creating Json token for remaining the login status of user. It can be also used to recognize the user.

#Second library is for encoding and decoding the password using SHA-256.
### We will continue to pursue this project till we can launch a fullly-functional community of parents.
