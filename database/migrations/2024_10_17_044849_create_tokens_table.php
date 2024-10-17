<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up()
    {
        Schema::create('tokens', function (Blueprint $table) {
            $table->id(); // Auto-incrementing ID
            $table->string('token')->unique(); // Unique token
            $table->foreignId('deck_id')->constrained('decks')->onDelete('cascade'); // Foreign key to decks
            $table->timestamp('expires_at')->nullable(); // Expiration timestamp
            $table->timestamps(); // Created at and updated at timestamps
        });
    }

    public function down()
    {
        Schema::dropIfExists('tokens'); // Drop the tokens table
    }
};
