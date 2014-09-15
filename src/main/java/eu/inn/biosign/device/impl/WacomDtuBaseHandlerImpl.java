package eu.inn.biosign.device.impl;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JWindow;

import eu.inn.biosign.BioSign;
import eu.inn.biosign.device.VisibleDeviceHandler;
import eu.inn.biosign.device.config.DeviceConfig;
import eu.inn.biosign.device.config.impl.WacomDtuDeviceConfigImpl;
//import java.awt.Frame;

public abstract class WacomDtuBaseHandlerImpl extends VisibleDeviceHandler {

	

	public WacomDtuBaseHandlerImpl() {		
		super();
	}


	@Override
	protected void stopCapturing() {
		// TODO Auto-generated method stub
		isCapturing = false;
	}
	
	@Override
	public void destroy() {
	}

	static JWindow f;

	protected static GraphicsDevice getGraphicsDevice() {
		GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		devices: for (GraphicsDevice device : environment.getScreenDevices()) {
			for (DisplayMode dMode : device.getDisplayModes()) {
				if (dMode.getWidth() != 1280 || dMode.getHeight() != 800)
					continue devices;
			}
			System.out.println(device.getIDstring() + " IS WACOM");
			return device;
		}
		return null;
	}

	@Override
	public void init() {

		try {
			GraphicsDevice d = getGraphicsDevice();
			if (d == null)
				throw new IllegalStateException("Cannot find WACOM Display");
			f = new JWindow();
			System.out.println(d.getIDstring() + " IS WACOM");
			f.setLocation(d.getDefaultConfiguration().getBounds().x, d.getDefaultConfiguration()
					.getBounds().y);
			f.setSize(d.getDefaultConfiguration().getBounds().width, d.getDefaultConfiguration()
					.getBounds().height);
			f.getContentPane().add(getVisibleJpanel());
			f.setVisible(true);
			f.setAlwaysOnTop(true);
			f.requestFocus();
		} catch (Throwable e) {
			e.printStackTrace();
			BioSign.showError(e);
		}
	}

	boolean isCapturing = false;

	@Override
	public void acquireNext() {
		try {
			clear();
			isCapturing = true;
		} catch (Exception e) {
			BioSign.showError(e);
		}

	}


	
	
	@Override
	public DeviceConfig createDeviceConfig() {
		return new WacomDtuDeviceConfigImpl();
	}


}
