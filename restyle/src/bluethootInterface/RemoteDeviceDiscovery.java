package bluethootInterface;

/*The LocalDevice class provides method 'getDiscoveryAgent' that returns an instance of the DiscoveryAgent. This DiscoveryAgent can then be used to discover remote bluetooth devices (start HCI inquiry).
 *
 */
import java.io.IOException;
import java.util.Vector;

import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;

/**
 * Minimal Device Discovery example.
 */
public class RemoteDeviceDiscovery {

	public static final Vector/* <RemoteDevice> */devicesDiscovered = new Vector();

	public static void main(final String[] args) throws IOException,
			InterruptedException {

		final Object inquiryCompletedEvent = new Object();

		devicesDiscovered.clear();

		final DiscoveryListener listener = new DiscoveryListener() {

			@Override
			public void deviceDiscovered(final RemoteDevice btDevice,
					final DeviceClass cod) {
				System.out.println("Device " + btDevice.getBluetoothAddress()
						+ " found");
				devicesDiscovered.addElement(btDevice);
				try {
					System.out.println("     name "
							+ btDevice.getFriendlyName(false));
				} catch (final IOException cantGetDeviceName) {
					//
				}
			}

			@Override
			public void inquiryCompleted(final int discType) {
				System.out.println("Device Inquiry completed!");
				synchronized (inquiryCompletedEvent) {
					inquiryCompletedEvent.notifyAll();
				}
			}

			@Override
			public void servicesDiscovered(final int transID,
					final ServiceRecord[] servRecord) {
				//
			}

			@Override
			public void serviceSearchCompleted(final int transID,
					final int respCode) {
				//
			}

		};

		synchronized (inquiryCompletedEvent) {
			final boolean started = LocalDevice.getLocalDevice()
					.getDiscoveryAgent()
					.startInquiry(DiscoveryAgent.GIAC, listener);
			if (started) {
				System.out.println("wait for device inquiry to complete...");
				inquiryCompletedEvent.wait();
				System.out.println(devicesDiscovered.size()
						+ " device(s) found");
			}
		}
	}

}
