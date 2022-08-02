JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	circleHough/Main.java \
	circleHough/Utils.java \
	circleHough/filters/GaussianBlur.java \
	circleHough/filters/Sobel.java \
	circleHough/houghTransform/Circle.java \
	circleHough/houghTransform/CircleDetector.java 
        
default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class