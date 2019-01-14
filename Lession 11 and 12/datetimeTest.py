import datetime
import time
import keyword

if __name__ == "__main__":
    print (datetime.datetime.now)
    print(time.time())
    print("%s" % time.time())
    print(keyword.iskeyword("async"))
    String = "Hell/oWorld"
    print String.find('/')
    String = String[String.find('l'):String.find('o')]
    print sum([1,2,3,4])
    print String
