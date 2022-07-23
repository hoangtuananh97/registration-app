# Registration-app

### Setup run
#### Step 1, You need to set up a mail to send mail. 

Open resources' folder, then application.properties files, You add:

> support.email=EMAIL@gmail.com (1)
> spring.mail.username=EMAIL@gmail.com (2)
> spring.mail.password=PASS (3)

(1) and (2) are the your address.

(3) is password of your gmail (this is tutorial https://www.lifewire.com/get-a-password-to-access-gmail-by-pop-imap-2-1171882)

#### Step 2, Setup language(if you need)
Open resources' folder, then resources Bundle 'message' and add your language
#### Finally, You run app.

### Flow test api with Postman
1. You need to run registration new account.
2. You will have a mail to confirm. Then, you copy link in mail and run in postman with Post method.
3. Finally, You can run apis to find all accounts, find an account by ID or update an account.

