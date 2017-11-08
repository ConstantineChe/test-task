#### Problem 1: Kind of FizzBuzz
##### description
This problem is a simple print numbers from 0 to 100, but with a twist.
The twist is: if the number contains 3 (like 3, 13, 30, etc) print &quot;Fizz&quot; instead of the number. Also, if the
number contains 5 print &quot;Buzz&quot;. If the number contains both 3 and 5, print &quot;FizzBuzz&quot;.
Note that this problem is similar, but not identical , to common FizzBuzz
(https://en.wikipedia.org/wiki/Fizz_buzz) problem.

##### Solution

FizzBazz solution result can be found <a href="#fizzbazz">here</a>

#### Problem 2: Random users
##### description

Problem 2: Random users
This problem requires use of external API. You have to familiarize yourself with the remote API and
then be able to request data from it.
Let&#39;s say that we need some example users for testing purposes. Instead of typing test users
manually, we&#39;d like to use an external service to generate random user profiles.
Your task is to create a function that returns a sequence of random users. Each returned user should
be a map with keys :name and :email . The value for :name should be user&#39;s full name with
first name followed by surname.
Your solution should use the API of the free https://randomuser.me/ service. If you are not familiar
with this service, check their documentation first.
Your function can take the number of users to return as an argument, or for full points, return an
infinite lazy sequence of users.

##### Solution
Created a function that requests 150 users from API, reads json, concatenates first name and last name. 
And a second function that creates a lazy-seq. 
Users are <a href="#/users">Here</a>

#### Problem 3: Web interface

##### description

Create a Web application that can show some (for example 10) random users from your solution to
problem 2. The target is to serve a page that works on modern web browsers.
You can generate the page using any tools you like. For full points you should use ClojureScript, but if
you are not familiar with it you can serve static HTML page too.
Please include the instructions on how to start your application in INFO.md file.

##### Solution

Web interface is done using luminus template with reagent and re-frame on frontend. 
Users are taken from a lazy seq through a core.async chan.
