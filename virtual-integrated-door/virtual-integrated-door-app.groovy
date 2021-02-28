/**

 */

definition(
    name: 'Virtual Integrated Door Device',
    namespace: 'vidd',
    author: 'Doug Gabehart',
    description: 'An integrated lock and contact sensor',
    category: 'Convenience',
    iconUrl: '',
    iconX2Url: ''
)

preferences {
    section('Choose the lock device associated with this door') {
        input 'lock', 'capability.lock', title: 'Door lock', required: true
    }

    section('Choose the contact sensor that detects whether the door is opened or closed') {
        input 'doorSensor', 'capability.contactSensor', title: 'Contact sensor attached to the door', required: true
    }
    section('Choose the Virtual integrated door device ') {
        input 'virtualLock', 'capability.lock', title: 'Virtual door device', required: true
        input 'virtualContactSensor', 'capability.garageDoorControl', title: 'Same virtual door device you just selected above', required: true
    }
}

def installed() {
    installer()
}
def updated() {
    unsubscribe()
    installer()
}
def installer() {
    log.info('Ensuring door is locked during install')
    lock.lock()
    virtualLockSetter('locked')
    log.info('Assuming virtual door contact sensor is closed')
    virtualContactSensorSetter('closed')

    subscribe(doorSensor, 'contact', doorSensorHandler)
    subscribe(lock, 'lock', lockHandler)
    subscribe(virtualLock, 'lock', virtualLockHandler)
}
def doorSensorHandler(event) {
    virtualContactSensorSetter(event.value)
}
def virtualLockHandler(event) {
    log.info('Handling lock event')
    if ('unlocking' == event.value) {
        lock.unlock()
    }
    else if ('locking' == event.value) {
        lock.lock()
    }
}

def lockHandler(event) {
    virtualLockSetter(event.value)
}
private def virtualLockSetter(value) {
    log.info("Lock event with value $value")

    if (value == 'locked' || value == 'locking') {
        virtualLock.lock(value)
    }
    else if (value == 'unlocked' || value == 'unlocking') {
        virtualLock.unlock(value)
    }
}
private virtualContactSensorSetter(value) {
    log.info("Handling door sensor event: $value")

    if ('open' == value) {
        virtualContactSensor.open(value)
    }
    else if ('closed' == value) {
        virtualContactSensor.close(value)
    }
}
