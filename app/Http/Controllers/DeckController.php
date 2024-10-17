<?php

namespace App\Http\Controllers;

use App\Models\Deck;
use Illuminate\Http\Request;
use Illuminate\Support\Str;

class DeckController extends Controller
{
    // Create a new deck
    public function store(Request $request)
    {
        // Validate the request to ensure 'deck' is present
        $request->validate([
            'deck' => 'required|string|max:255',
        ]);

        // Generate a unique token
        $token = Str::random(60);

        // Create a new deck with the 'deck' field and the generated token
        $deck = Deck::create([
            'deck' => $request->input('deck'),
            'token' => $token,
            'token_expires_at' => now()->addHour(),
        ]);

        // Return the deck along with the generated token
        return response()->json([
            'deck' => $deck,
            'token' => $token,
        ], 201);
    }

    // Retrieve all decks
    public function index()
    {
        $decks = Deck::all(); 
        return response()->json($decks);
    }

    // Update the deck
    public function update(Request $request, $id)
    {
        $deck = Deck::find($id);
        if (!$deck) {
            return response()->json(['error' => 'Deck not found'], 404);
        }

        $request->validate([
            'deck' => 'required|string|max:255',
        ]);

        $deck->update([
            'deck' => $request->input('deck'),
        ]);

        return response()->json($deck);
    }

    // Delete deck
    public function destroy($id)
    {
        $deck = Deck::find($id);
        if (!$deck) {
            return response()->json(['error' => 'Deck not found'], 404);
        }

        $deck->delete();
        return response()->json(['message' => 'Deleted successfully']);
    }
}
