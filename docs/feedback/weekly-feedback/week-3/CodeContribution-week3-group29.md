# Code Contributions and Code Reviews


#### Focused Commits

Grade: Sufficient

Feedback: Most commit messages are concise one liners.  
Commits only affect a small number of files. However, some of them do not represent coherent or meaningful changes to the system.  
Your code should be well commented before you commit, so that you don't have commits/MRs with only JavaDoc for example or only changing one field of a class/method.  
For this week some members don't have commits at all (or no meaningful ones).  
Tip: Your commit messages should be descriptive but still concise. Make sure everybody has meaningful commits related to individual changes.


#### Isolation

Grade: Sufficient

Feedback: Since you didn't start coding that much, there is not much to judge on feature branches/MR.  
Tip: Make sure you name your branches descriptive per feature or per change, and don't use branches with your names or things like that.  
A good example would be MR nr 23. Try to keep that structure and isolate one feature per branch.  
It is a good start, I saw that you started isolating some features and also created a few different branches to work on (like "TaskList", "Task" etc).


#### Reviewability

Grade: Sufficient

Feedback: You have some MR into "main", but not enough yet to be able to judge on this criteria for all of the team members.  
MRs usually have a clear focus that is easy to understand from the title.  
Tip: Try to make the MRs clear and focused so they are easy to review. The changes should not be related to other features and always solve merge conflicts.


#### Code Reviews

Grade: Insufficient

Feedback: So far you have no comments or reviews.  
Tips: Make sure every MR has at least two constructive reviews from different members.
The reviews should lead to iterative improvements of the code and controversial changes should be discussed by more than two team members.  
Do not leave your MRs open for too long. Merge small and often so the code is checked frequently.


#### Build Server

Grade: Insufficient

Feedback: This part is related to the building of the Server, and again there is not much to judge now (on "main").  
Tips: Make sure you commit frequently so the pipeline runs often. You should not have a failing pipeline, especially on the "main" branch.  
In case of a failing pipeline fix it as soon as possible and test your code by building it before pushing. Also make sure you run the CheckStyle before you push, because this could also lead to pipeline fail.  
You should not have commits that change the formatting too often (things like repairing the checkstyle you forgot to run).
