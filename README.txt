Trader program version 1.0 06/26/2017

USAGE NOTES:
- To run this jar file, open a command-prompt/terminal and navigate to that folder. Now type
  java -jar NameOfJARFile.jar and press ENTER.

Functions supported by this jar file:
N  - Create a new basket and give it a specific name
     Create a new basket in the model and specify the name and creation date. If a basket with the
     same name has been created before, or if the specified creation date is not a business day,
     creation fails and option menu prompts.

A  - Add shares of stocks to an existing basket using its ticker symbol
     Add the specified amount of share to a specified basket. If the stock symbol is invalid, or if
     the specified share is negative, or if the specified basket does not exist, this operation
     fails and option menu prompts.

P  - Print the contents and values of an existing basket
     Print the contents and values of an existing basket. If the basket does not exist, this
     operation fails and option menu prompts.

TB - Trend of a particular basket within a specific data range
     Return the trend of a specified basket in certain date range. Positive value means this basket
     trends up in the given date range. If the date range is a weekend or a holiday, or if the
     specified basket does not exist, this operation fails and option menu prompts.

TS - Trend of a particular stock within a specific data range
     Return the trend of a specified stock in certain date range. Positive value means this stock
     trends up in the given date range. If the date range is a weekend or a holiday, or if the
     specified stock is invalid, this operation fails and option menu prompts.

G  - Open graphical view
     Prompt menu that specify the operations supported by the graphical view. Functions of the
     graphical view are explained below.

I - Try different investment strategies
    Prompt the menu that allows the user to do simulation on different investment strategies.
    Functions of the simulation are explained below.

Q  - Quit program
     Quick the main program.


Functions supported by the graphical view:
1 - add/remove a stock to visualize
    Add a stock to or remove a stock from the graphical view. If the stock is invalid, or if the
    user does not enter the content required by the prompt, this operation fails and graphical
    option menu prompts.

2 - add/remove one of your baskets to visualize
    Add a basket to or remove a basket from the graphical view. If the basket has not been created,
    or if the user does not enter the content required by the prompt, this operation fails and
    graphical option menu prompts.

3 - plot historical prices alone for the chosen stocks and baskets
    Ask the user for a date range and show the historical prices of the chosen stocks and baskets
    in the date range. If the given end date is before the given start date, or if the source file
    is not read correctly, an blank graph will appear and graphical option menu prompts.

4 - plot 50-day Moving Average alone for the chosen stocks and baskets
    Ask the user for a date range and show the 50-day moving averages of the chosen stocks and
    baskets in the date range. If the given end date is before the given start date, or if the
    source file is not read correctly, an blank graph will appear and graphical option menu prompts.

5 - plot 200-day moving average alone for the chosen stocks and baskets
    Ask the user for a date range and show the 200-day moving averages of the chosen stocks and
    baskets in the date range. If the given end date is before the given start date, or if the
    source file is not read correctly, an blank graph will appear and graphical option menu prompts.

6 - plot historical prices AND 50-day moving average
    Ask the user for a date range and show the historical prices and 50-day moving averages of
    the chosen stocks and baskets in the date range. If the given end date is before the given
    start date, or if the source file is not read correctly, an blank graph will appear and
    graphical option menu prompts.

7 - plot historical prices AND 200-day moving average
    Ask the user for a date range and show the historical prices and 200-day moving averages of
    the chosen stocks and baskets in the date range. If the given end date is before the given
    start date, or if the source file is not read correctly, an blank graph will appear and
    graphical option menu prompts.

8 - plot 50-day moving average AND 200-day moving average
    Ask the user for a date range and show the 50-day and 200-day moving averages of
    the chosen stocks and baskets in the date range. If the given end date is before the given
    start date, or if the source file is not read correctly, an blank graph will appear and
    graphical option menu prompts.

9 - draw blank graph
    Plot a blank graph and clear stocks and baskets that needs to be plotted.

0 - Quit the program.
    Quick graphical view and return to the main menu.


Functions supported by simulation:
Ask the user for the following information and return the profit that would be earned if the
specified strategy were followed.

- Basket name: The name of a new basket to be simulated. This basket is independent from the
  baskets (if any) created in the main menu option.

- Creation date of the basket: The creation date of the basket to be simulated. If the given date
  is not a business day, this operation fails and the main menu prompts.

Repeatedly ask the user to the following information until the user chooses 'q' to finish adding:
- Symbol of the stock to be added to this basket.
- Share of this stock to be added to this basket.
If the symbol is invalid, or if the specified share is negative, this operation fails and the main
menu prompts.

- Investment strategy: for now only the dollar-cost averaging strategy is supported. Invalid
  options will fail this operation and the main menu prompts.

- Amount of money to be invested each time: this value should be a positive number, otherwise this
  operation fails and the main menu prompts.

- Date when the investment starts: ask the user for a business day that he/she would like to start
  the investment, this date should not be prior to the creation date of the basket, otherwise this
  operation fails and the main menu prompts.

- Date when the investment ends: ask the user for date that he/she would like to end
  the investment, this date should not be prior to the start date of the investment, otherwise this
  operation fails and the main menu prompts.

- Investment period: how often does the user want to invest based on days. This valid should be a
  number not less than 7, otherwise this operation fails and the main menu prompts.

- Date when the profit will be calculated: ask the user for the date he/she wants to know the
  profit, this date should be a business day not be prior to the end date of the investment,
  otherwise this operation fails and the main menu prompts.

The estimate profit will appear and the main menu prompts after the above information is entered
correctly.