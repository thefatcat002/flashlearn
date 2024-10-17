<?php

namespace App\Http\Controllers;

use App\Models\Question;
use Illuminate\Http\Request;

class QuestionController extends Controller
{
    // Create new question
public function store(Request $request)
{
    // Validate the request data
    $request->validate([
        'question' => 'required|string|max:255', // Ensure 'question' is required and is a string
        'answer' => 'nullable|string', // 'answer' is optional
    ]);

    // Create the question
    $question = Question::create([
        'question' => $request->input('question'),
        'answer' => $request->input('answer', null),  
    ]);

    // Return the created question with a 201 status code
    return response()->json($question, 201);
}


    // Retrieve all questions
    public function index()
    {
        $questions = Question::all();
        return response()->json($questions);
    }

    // Update  question or answer
    public function update(Request $request, $id)
    {
        $question = Question::find($id);
        if (!$question) {
            return response()->json(['error' => 'Question not found'], 404);
        }

        $question->update([
            'question' => $request->input('question', $question->question),
            'answer' => $request->input('answer', $question->answer),
        ]);

        return response()->json($question);
    }

    // Delete question
    public function destroy($id)
    {
        $question = Question::find($id);
        if (!$question) {
            return response()->json(['error' => 'Question not found'], 404);
        }

        $question->delete();
        return response()->json(['message' => 'Deleted successfully']);
    }

   
}

