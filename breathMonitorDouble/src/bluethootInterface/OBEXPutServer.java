package bluethootInterface;

/*This class will start an OBEX service that will accept Put commands and print it to standard out.
 * 
 */

import java.io.IOException;
import java.io.InputStream;

import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.microedition.io.Connector;
import javax.obex.HeaderSet;
import javax.obex.Operation;
import javax.obex.ResponseCodes;
import javax.obex.ServerRequestHandler;
import javax.obex.SessionNotifier;

public class OBEXPutServer {

	private static class RequestHandler extends ServerRequestHandler {

		public RequestHandler() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public int onPut(final Operation op) {
			try {
				final HeaderSet hs = op.getReceivedHeaders();
				final String name = (String) hs.getHeader(HeaderSet.NAME);
				if (name != null) {
					System.out.println("put name:" + name);
				}

				final InputStream is = op.openInputStream();

				final StringBuffer buf = new StringBuffer();
				int data;
				while ((data = is.read()) != -1) {
					buf.append((char) data);
				}

				System.out.println("got:" + buf.toString());

				op.close();
				return ResponseCodes.OBEX_HTTP_OK;
			} catch (final IOException e) {
				e.printStackTrace();
				return ResponseCodes.OBEX_HTTP_UNAVAILABLE;
			}
		}
	}

	static final String serverUUID = "11111111111111111111111111111123";

	public static void main(final String[] args) throws IOException {

		LocalDevice.getLocalDevice().setDiscoverable(DiscoveryAgent.GIAC);

		final SessionNotifier serverConnection = (SessionNotifier) Connector
				.open("btgoep://localhost:" + serverUUID + ";name=ObexExample");

		int count = 0;
		while (count < 2) {
			final RequestHandler handler = new RequestHandler();
			serverConnection.acceptAndOpen(handler);
			System.out.println("Received OBEX connection " + (++count));
		}
	}
}
