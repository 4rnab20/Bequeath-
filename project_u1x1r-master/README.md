# *Bequeath* - A donation tracker

## Overview:
This application can be used to keep track of your donations. This app will allow the user to create a list of different 
charities to be made and the type of charity. Namely, a user can create a charity list and the amount of donation for 
each donation. This app is for people who donate quite frequently and needs to keep record of their transactions.

## Personal regard:
Currently, people are donating quite frequently, especially due to COVID-19. I believe many people often lose track of 
what they donated or how much which might cause them problems in the future, such as donating to the same place twice
unintentionally. Personally, I tend to forget the amount of money donated or the type of donation. I believe this app
will help me and other people as well in keeping track of the donations.

## User Stories:
- As a user, I want to be able to add a donation to my list of donations.
- As a user, I want to be able to change a donation using a donation ID.
- As a user, I want to be able to see the list of my donation as completed.
- As a user, I want to be able to view the list of the types and total amount of donation in my list.
- As a user, I want to be able to select a donation in my list and view it in detail using its ID.
- As a user, I want to be given the option to display a bar chart of donations.
- As a user, when I select the quit option from the menu, I want to be reminded to save my donation list to file and have the option to do so or not.
- As a user, when I start the application, I want to be given the option to load my donation list from file.

## Phase 4: Task 2:
- The classes Donation and DonationList have a robust design. 
- The methods addDonation(Donation newDonation), searchDonation(int id), and changeDonation(int id, double amount) have a robust design.
- The Main UI (CLI) deals with the exceptions.

## Phase 4: Task 3:
- Would want to make the methods more efficient and cleaner(using fewer lines of code)