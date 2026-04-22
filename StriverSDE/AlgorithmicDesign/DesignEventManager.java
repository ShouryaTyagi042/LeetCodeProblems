class EventManager {

    static class Event {
        int id ;
        int priority ;

        Event(int id, int priority) {
            this.id = id ;
            this.priority = priority ;
        }
    }

    TreeSet<Event> set = new TreeSet<Event>();
    HashMap<Integer, Event> map = new HashMap<>();

    public EventManager(int[][] events) {
        // Initialisation
        set = new TreeSet<>((a,b) -> {
            if(a.priority != b.priority) {
                return b.priority - a.priority ;
            }
            return a.id - b.id ;
        })
        map = new HashMap<>();
        for(int[] e : events) {
            Event event = new Event(e[0], e[1]) ;
            set.add(event);
            map.put(e[0], event) ;
        }
    }

    public void updatePriority(int eventId, int newPriority) {
        Event staleEvent = map.get(eventId);
        Event updatedEvent = new Event(eventId, newPriority) ;
        if(map.containsKey(eventId)) {
            map.put(eventId, updatedEvent) ;
        }
        if(set.contains(staleEvent)) {
            set.remove(staleEvent) ;
            set.add(updatedEvent) ;
        }
    }

    public int pollHighest() {
        if (set.isEmpty()) return -1;

        Event top = set.pollFirst(); // highest priority
        map.remove(top.id);

        return top.id;
    }
}

/**
 * Your EventManager object will be instantiated and called as such:
 * EventManager obj = new EventManager(events);
 * obj.updatePriority(eventId,newPriority);
 * int param_2 = obj.pollHighest();
 */
