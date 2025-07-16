package com.example.CorporateBankLoanSystem.service;

import org.springframework.stereotype.Service;
import java.util.Random;

@Service
public class CibilService {

    private Random random = new Random();

    public int generateSimulatedCibilScore() {
        // Simple random score between 300 and 900 for simulation purposes
        return random.nextInt(601) + 300;
    }
}
