| Method / Behavior | Valid Case(s) | Exception / Invalid Case(s) | Boundary Case(s) | Oracle / Expected Result | Related JUnit Test(s) |
|---|---|---|---|---|---|
| - | ----------- | ----------- | ------------| ---------------- | --------------- |
|getSlotIndex| Code: A, B, C, D | Code is none of those | Code == -A, or F | Program should return the specific index (example A = 0) | -------------- |
|addItem| Code: A and Item: Chicken and slot is empty | When the machine is not empty at that slot or an invalid index code | item full and/or wrong code like F | Program must return with the slot being full, will be tested with a equals for the name of the item|-----------|
|getItem| Slot A is occupied| The code is invalid | Code= -A or F| Program should return what is in that slot, so if chicken should see chicken | ----------------|
|removeItem| Slot A is full and is now removed/unoccupied| slot is already empty | Check code -A, F, and a unoccupied slot| Program must return the emptied slot, if A was chicken must return nothing| ----------- | |insertMoney | You have a increased balance | You tried to insert less than a penny | insert 0.01 or -0.0| program must return current balance with the new money, example 100 + 1 = 101 new balance. When testing will PARAMETERIZED TESTING TO test all bounds | ---------- | |getBalance | can see user balance in machine 0 too | seeing negative numbers or innacurate bal | return -0.01 | Program must return the balance in machine, if inserted a penny bal should be one penny | ---------------------| 
|makePurcahse | code has item and enough money is met | Enough money not met or empty slot | empty code, or item cost: $1 and bal = $0.99 should return exception | if item cost $1 and $2 is in, the bal must be $1 now and the item must be given to user | ----------| 
|returnChange | gives the change and empty machine | Not giving correct change | return 0.01, try to return -0.01 | if a $1 is left the program will return a $1 and empty machine to 0 | --------- |
|VendingMachineItem | name exist and price >= 0 | price is less than 0| -0.01 item cost or unamed item | Chicken with $1 should be planted inside machine | ----------- | 
|  |
