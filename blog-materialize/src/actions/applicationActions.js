export const CONNECT = "CONNECT";
export const DISCONNECT = "DISCONNECT";
export const CONNECTED = "CONNECTED";
export const DISCONNECTED = "DISCONNECTED";

export function connected() {
	return {
		type: CONNECTED
	}
}

export function disconnected() {
	return {
		type: DISCONNECTED
	}
}

export function connectToWS() {
	return {
		url: `wss://${window.location.hostname}`,
		type: CONNECT
	}
}

export function disconnectFromWS() {
	return {
		type: DISCONNECT
	}
}
