package org.springframework.samples.petclinic;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class AnnoyingErroneousService {

	@HystrixCommand(fallbackMethod = "getFallbackValue")
	public long getRandomValue() {
		final Random rand = new Random(System.currentTimeMillis());
		final long randLong = rand.nextLong();
		final int x = (int) randLong % 7;
		switch (x) {
		case 0:
			throw new RuntimeException("Der Wert ist durch 7 teilbar, das ist ja echt blöd ...");
		case 1:
			// Fallthrough!
		case 2:
			try {
				Thread.sleep(ThreadLocalRandom.current().nextLong(50, 5000));
			} catch (final InterruptedException e) {
				System.err.println("Unhöflich! Beim Warten unterbrochen ... " + e.getMessage());
			}
			return randLong;
		default:
			return randLong;
		}
	}

	public long getFallbackValue() {
		return 42L;
	}
}
