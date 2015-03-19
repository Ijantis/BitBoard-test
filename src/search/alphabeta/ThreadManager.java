package search.alphabeta;

import java.util.ArrayList;

import movegen.MoveGenerator;
import board.FullGameState;

public class ThreadManager {

	public static FullGameState alphaBetaThreaded(
			FullGameState currentGameState, boolean whiteToMove) {

		ArrayList<FullGameState> nextDepth;
		ArrayList<AlphaBetaThread> searchThreads = new ArrayList<AlphaBetaThread>();
		if (whiteToMove) {
			nextDepth = MoveGenerator.generateWhiteLegalMoves(currentGameState);
		} else {
			nextDepth = MoveGenerator.generateBlackLegalMoves(currentGameState);
		}

		int depth = 4;
		int moveOrderingDepth = 2;

		if (whiteToMove) {
			System.out.println("white to move");
			System.out.println("There are " + nextDepth.size()
					+ " moves available");
			int bestIndex = 0;
			long bestValue = Integer.MIN_VALUE;
			long currentScore;

			System.out.println("Creating threads:");
			for (int i = 0; i < nextDepth.size(); i++) {
				System.out.print(i + " ");
				AlphaBetaThread nextThread = new AlphaBetaThread(
						nextDepth.get(i), depth, Integer.MIN_VALUE,
						Integer.MAX_VALUE, whiteToMove, moveOrderingDepth);
				nextThread.start();
				searchThreads.add(nextThread);
			}
			System.out.println();

			System.out.println("Waiting on threads....");
			for (int i = 0; i < searchThreads.size(); i++) {
				try {
					searchThreads.get(i).join();
					System.out.println("Thread finished " + i);
					currentScore = searchThreads.get(i).getScore();
					if (currentScore > 100000) {
						System.out
								.println("Forced mate found. Terminating threads");
						for (int j = i; j < searchThreads.size(); j++) {
							searchThreads.get(j).interrupt();
						}
						return nextDepth.get(i);
					}

					if (currentScore > bestValue) {
						System.out.println("New best score " + i);
						bestValue = currentScore;
						bestIndex = i;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return nextDepth.get(bestIndex);

		} else {
			System.out.println("Black to move");
			System.out.println("There are " + nextDepth.size()
					+ " moves available");
			int bestIndex = 0;
			long bestValue = Integer.MAX_VALUE;
			long currentScore;

			System.out.println("Creating threads:");
			for (int i = 0; i < nextDepth.size(); i++) {
				System.out.print(i + " ");
				AlphaBetaThread nextThread = new AlphaBetaThread(
						nextDepth.get(i), depth, Integer.MAX_VALUE,
						Integer.MIN_VALUE, whiteToMove, moveOrderingDepth);
				nextThread.start();
				searchThreads.add(nextThread);
			}
			System.out.println();

			System.out.println("Waiting on threads....");
			for (int i = 0; i < searchThreads.size(); i++) {
				try {
					searchThreads.get(i).join();
					System.out.println("Thread finished " + i);
					currentScore = searchThreads.get(i).getScore();
					if (currentScore < -100000) {
						System.out
								.println("Forced mate found. Terminating threads");
						for (int j = i; j < searchThreads.size(); j++) {
							searchThreads.get(j).interrupt();
						}
						return nextDepth.get(i);
					}

					if (currentScore < bestValue) {
						System.out.println("New best score " + i);
						bestValue = currentScore;
						bestIndex = i;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return nextDepth.get(bestIndex);
		}

	}
}
