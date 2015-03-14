Jiexi Luan
Andy Yoon
Team: Last Minute
Project 5

Q1: (4)->(5) and (5)->(6)

Q2: Our connection between OAK and Tomcat must be secured because this is 
the only point at which the buy_price and other item info is transmitted 
and stored in the session for the Tomcat server. Afterwards, the user on 
the Tomcat server is given a session cookie that is simply a token that 
tells the server which session on the server this user is associated with, 
but not the contents of the session on the server. Worst case, the user is 
able to purchase items in other user's sessions by guessing other user's 
cookie token, but they would still be purchasing it at that item's correct 
buy_price.


