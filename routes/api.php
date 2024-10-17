<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\QuestionController;
use App\Http\Controllers\DeckController;

#Q&A CRUD
Route::get('/questions', [QuestionController::class, 'index']); 
Route::post('/questions', [QuestionController::class, 'store']); 
Route::put('/questions/{id}', [QuestionController::class, 'update']);  
Route::delete('/questions/{id}', [QuestionController::class, 'destroy']); 

#Decks CRUD
Route::get('/decks', [DeckController::class, 'index']);
Route::post('/decks', [DeckController::class, 'store']);
Route::put('/decks/{id}', [DeckController::class, 'update']);
Route::delete('/decks/{id}', [DeckController::class, 'destroy']);


Route::get('/user', function (Request $request) {
    return $request->user();
})->middleware('auth:sanctum');
