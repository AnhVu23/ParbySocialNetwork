# Parby Social Network

Brought to you by ANT Team

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

### We will continue to pursue this project till we can launch a fullly-functional community of parents.
