Initial Prompt with Chatgpt 6 sol: https://chatgpt.com/share/6ac9f1e8-32f8-83e9-a964-00e50ddd3d28

You are to use JUnit 5 to create reasonable test that will test nearly everything and leave nothing out of coverage. the calsses you are testing are. 

VendingMachineItem.java
VendingMachine.java

only these two. Create two files (the filename + test at the end) so that we can run it with the testing tool in VS CODE. Naturally follow the arrange act and assert formula but of course if you have a simplier way to do it or better way be my guest just document it..



You are to use JUnit 5 to create reasonable test that will test nearly everything and leave nothing out of coverage. the calsses you are testing are. 

VendingMachineItem.java
VendingMachine.java

only these two. Create two files (the filename + test at the end) so that we can run it with the testing tool in VS CODE. Naturally follow the arrange act and assert formula but of course if you have a simplier way to do it or better way be my guest just document it..


Pasted above is the first prompt i put into chatgpt along with the source code (with the bugs) om vending machine. Now I realize I should've gave it a vauger prompt for the first time because it gave me a 100% statmenet, decision and condition coverage. So there wasnt much work needed to be done other than the abundant amount of test it gave me. My original test file for vending machine is a little shorter than 200 lines with max coverage but the AI version is almost 300 lines which is just not efficient. Now I gave it another prompt to make the test more efficient and remove unnessasry test while having maximum coverage and this is so much better. Only 13 test in total yet we have the maximum level of coverage. Therefore from my conclusions I can say AI doesnt do a bad job necessarily, it just needs some efficencyhandling after the initial test are made. None of my test failed so the AI was able to understand that atleast. Now the quality of the work the AI did was great but it is a little hard to read. compared to the ones i made it seems the AI tried to fit some test together which ended up working anyway it just makes it really hard to read, especially when compared to my personally written code..