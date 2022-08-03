JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	circleHough/*.java \
	circleHough/filters/*.java \
	circleHough/houghTransform/*.java
        
default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
