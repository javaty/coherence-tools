package com.seovic.coherence.identity;


import java.util.*;


/**
 * Multi-threaded test client for IdentityGenerator.
 *  
 * @author Aleksandar Seovic  2008.11.24
 * @version 1.0
 */
@SuppressWarnings({"unchecked", "EmptyCatchBlock"})
public class IdentityGeneratorClient {

    private IdentityGenerator generator;
    private List<Thread> workers = new ArrayList<Thread>();

    public IdentityGeneratorClient(IdentityGenerator generator) {
        this.generator = generator;
    }

    public Set generateIdentities(int numThreads, int numIdentities) {
        Set identities = Collections.synchronizedSet(new HashSet());

        for (int i = 0; i < numThreads; i++) {
            Thread worker = new IdentityGeneratorThread(identities, numIdentities);
            workers.add(worker);
            worker.start();
        }

        for (Thread worker : workers) {
            try { worker.join(); } catch (InterruptedException e) {}
        }

        return identities;
    }

    class IdentityGeneratorThread extends Thread {

        private Set identities;
        private int numIdentities;
        private Random randomizer;

        public IdentityGeneratorThread(Set identities, int numIdentities) {
            this.identities = identities;
            this.numIdentities = numIdentities;
            this.randomizer = new Random();
        }

        public void run() {

            for (int i = 0; i < numIdentities; i++) {
                identities.add(generator.generateIdentity());
                try { Thread.sleep(randomizer.nextInt(10)); }
                catch (InterruptedException e) {}
            }
        }
    }
}
