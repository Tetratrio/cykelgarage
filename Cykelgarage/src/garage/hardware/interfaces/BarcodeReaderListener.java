package garage.hardware.interfaces;

public interface BarcodeReaderListener {
	public void entryBarcode(String code);
	public void exitBarcode(String code);
}
