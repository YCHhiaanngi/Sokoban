# Sokoban
Sokoban is a game that tests player's reasoning skills. 

## Rules

You can control the hero to push the box in the grid in four directions. As long as all of the boxes are on the right position, you win. If all of the boxes have no way to go, you will lose.

## Instructions to this Game

### User System

If you want to save your progress in the game, you need an account. You can register first if you are new or log in if you have already had an account. Your progress will be saved automatically every few seconds. And you can load your progress the next time you play the same level. Your progress will include the levels you have passed with time and step, the status of the levels that you have not passed.

If you do not need these functions, please choose GUEST MODE and the progress will not be saved.

Please do not change the content of files under the folder `userfile`, this may trigger glitches. If your file is distorted by others, we will warn you before you load your file.

### Operations

Press `UP`, `DOWN`, `LEFT`, `RIGHT` keys on your keyboard or press the button on the right of the game window to control the movement of hero. When you finished 

### AI Solver

If you want to know what is the optimal path to win, an AI solver would be helpful. It will give you the least steps and the amount of the steps quickly. In the path that the AI offers to you, **L** means to move left, **R** means to move right, **U** means to move up, **D** means to move down.

**BE CAREFUL** , the AI solver cannot be used when the hero is on the goal block, this may result in wrong answers.

### 拿中文再重复一遍

这个仓库我决定开源了，给后面的学弟学妹们一点微不足道的帮助，也算是一种互助精神的传递。（但是我还是建议自己写，工程能力是靠大量实践积累起来的，上课不会教你怎么去管理好一个大项目）

特别提醒一下：这个AI是有bug的，站在目标方格上就会显示没有最佳路径，我也不知道是bfs本身的局限还是我写的代码有问题，不过我懒得修了。

最后答辩只拿了`95`分，因为自动存档功能也是有bug的，开局自动存档，导致你来不及读档，而且也没有做存档防修改，不符合要求。（不过我现在放假了就懒得修这个bug了（小声））

最后给后面学JavaA的同学一点建议：善用AI、善用Stack Overflow（CSDN、博客园等也可）、善用开源项目（老师只关心你的最终效果，不在意过程，不是说纵容抄袭，而是要利用好已有的资源）
