package bluethootInterface;

/*The following example shows how to use the DiscoveryAgent to find OBEX Push bluetooth service. Class from DeviceDiscovery example is used to find Bluetooth Devices.
 * 
 */
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import javax.bluetooth.DataElement;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;

/**
 * 
 * Minimal Services Search example.
 */
public class ServicesSearch {

	static final UUID OBEX_FILE_TRANSFER = new UUID(0x1106);

	public static final Vector/* <String> */serviceFound = new Vector();

	// cos'e'?
	private static final UUID OBEX_OBJECT_PUSH = new UUID(0x1105);

	public static void main(final String[] args) throws IOException,
			InterruptedException {

		// First run RemoteDeviceDiscovery and use discoved device
		RemoteDeviceDiscovery.main(null);

		serviceFound.clear();

		UUID serviceUUID = OBEX_OBJECT_PUSH;
		if ((args != null) && (args.length > 0)) {
			serviceUUID = new UUID(args[0], false);
		}

		final Object serviceSearchCompletedEvent = new Object();

		final DiscoveryListener listener = new DiscoveryListener() {

			@Override
			public void deviceDiscovered(final RemoteDevice btDevice,
					final DeviceClass cod) {
			}

			@Override
			public void inquiryCompleted(final int discType) {
			}

			@Override
			public void servicesDiscovered(final int transID,
					final ServiceRecord[] servRecord) {
				for (int i = 0; i < servRecord.length; i++) {
					final String url = servRecord[i].getConnectionURL(
							ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
					if (url == null) {
						continue;
					}
					serviceFound.add(url);
					final DataElement serviceName = servRecord[i]
							.getAttributeValue(0x0100);
					if (serviceName != null) {
						System.out.println("service " + serviceName.getValue()
								+ " found " + url);
					} else {
						System.out.println("service found " + url);
					}
				}
			}

			@Override
			public void serviceSearchCompleted(final int transID,
					final int respCode) {
				System.out.println("service search completed!");
				synchronized (serviceSearchCompletedEvent) {
					serviceSearchCompletedEvent.notifyAll();
				}
			}

		};

		final UUID[] searchUuidSet = new UUID[] { serviceUUID };
		final int[] attrIDs = new int[] { 0x0100 // Service name
		};

		for (final Enumeration en = RemoteDeviceDiscovery.devicesDiscovered
				.elements(); en.hasMoreElements();) {
			final RemoteDevice btDevice = (RemoteDevice) en.nextElement();

			synchronized (serviceSearchCompletedEvent) {
				System.out.println("search services on "
						+ btDevice.getBluetoothAddress() + " "
						+ btDevice.getFriendlyName(false));
				LocalDevice
						.getLocalDevice()
						.getDiscoveryAgent()
						.searchServices(attrIDs, searchUuidSet, btDevice,
								listener);
				serviceSearchCompletedEvent.wait();
			}
		}

	}

}
