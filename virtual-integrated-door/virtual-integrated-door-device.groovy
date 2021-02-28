/**
 *  Virtual integrated door device
 */
metadata {
	definition (name: "Virtual integrated door device", namespace: "vidd", author: "Doug Gabehart") {          
		capability "Lock"		
        capability "Door Control"
        capability "Garage Door Control"
	}
    tiles {		        		
        standardTile("contact", "device.contact", width: 1, height: 1) {
			state("closed", label:'${name}', icon:"st.contact.contact.closed", backgroundColor:"#00A0DC")
			state("open", label:'${name}', icon:"st.contact.contact.open", backgroundColor:"#e86d13")
		}        
		main "toggle"
		details(["toggle", "lock", "unlock", "contact"])
	}    
}

def lock(value) {
    def eventValue = (value != null) ? value : "locking"
    log.info("lock value is $value")
    sendEvent(name: "lock", value: eventValue)
}

def unlock(value) {
    def eventValue = (value != null) ? value : "unlocking"
    log.info("unlock value is $value")
    sendEvent(name: "lock", value: eventValue)
}

def open(value) {
    def eventValue = (value != null) ? value : "opening"
    log.info("open value is $value")
    sendEvent(name: "door", value: "open")
}

def close(value) {
    def eventValue = (value != null) ? value : "closing"
    log.info("close value is $value")
    sendEvent(name: "door", value: "closed")
}

