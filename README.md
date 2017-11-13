# BFS-BigBananaFileSystem
An fatal distribute system like GFS(Google File System)

专门学习分布式文件系统用，没法上生产的。

现在bug很多，单机版本，所有的东西都没有写入文件持久化，只在内存持久化了。

参照Google File System实现的。
你只能试试看这样玩。

直接运行BFSClient就可以跑起来啦。

ls to list the file eg: ls /bfs 

get to get the file eg: get /bfs/some.log 

put to put a file eg: put 'some' /bfs/some.log 

delete to delete a file delete /bfs/some.log

if you want to exit the command , input :exit

put "kkk" /some/big.log

ls /some

put "abc" /some/a.log

ls /some

get /some/big.log

get /some/a.log
