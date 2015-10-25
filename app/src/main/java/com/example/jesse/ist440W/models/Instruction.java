package com.example.jesse.ist440W.models;

/**
 * Represents one instruction in a recipe.
 * Created by Jesse on 9/22/2015.
 */
public class Instruction {
    private int instructionId;
    private String instructions;
    private int orderId;

    public Instruction(int instructionId, String instructions, int orderId) {
        this.instructionId = instructionId;
        this.instructions = instructions;
        this.orderId = orderId;
    }

    public int getInstructionId() {
        return instructionId;
    }

    public void setInstructionId(int instructionId) {
        this.instructionId = instructionId;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}
