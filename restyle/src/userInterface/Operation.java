package userInterface;

import java.util.StringTokenizer;

public interface Operation {
	void callOp(StringTokenizer commandTokenizer, String line);
}
