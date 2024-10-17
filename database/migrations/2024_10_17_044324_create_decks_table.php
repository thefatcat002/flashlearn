<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up()
    {
        Schema::create('decks', function (Blueprint $table) {
            $table->id();
            $table->string('deck'); 
            $table->string('token')->unique();
            $table->timestamp('token_expires_at')->nullable();
            $table->timestamps();
        });
    }

    public function down()
    {
        Schema::dropIfExists('decks'); 
    }
};
