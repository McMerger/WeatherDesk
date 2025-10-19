"use server";

import type { WeatherData, WeatherState } from "@/lib/types";
import { z } from "zod";

function generateMockData(city: string): WeatherData {
  // Use a simple hash to get consistent "random" data for a city
  let hash = 0;
  for (let i = 0; i < city.length; i++) {
    hash = city.charCodeAt(i) + ((hash << 5) - hash);
  }

  const days = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];
  const conditions = ["Clear", "Clouds", "Rain", "Snow", "Thunderstorm", "Mist"];
  const now = new Date();
  
  const current = {
    city: city.split(',')[0].split(' ').map(word => word.charAt(0).toUpperCase() + word.slice(1)).join(' '),
    temperature: Math.round(10 + (Math.abs(hash) % 15) + Math.sin(now.getHours()) * 5),
    condition: conditions[Math.abs(hash) % conditions.length],
    humidity: 40 + (Math.abs(hash) % 40),
    windSpeed: 5 + (Math.abs(hash) % 15),
    date: now.toLocaleDateString('en-US', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' }),
  };

  const forecast = Array.from({ length: 5 }, (_, i) => {
    const dayIndex = (now.getDay() + i + 1) % 7;
    const dayHash = Math.abs(hash * (i + 1));
    const high = Math.round(current.temperature + 5 - (dayHash % 10));
    const low = high - Math.round(5 + (dayHash % 5));
    return {
      day: days[dayIndex],
      high: high,
      low: low,
      condition: conditions[Math.abs(dayHash) % conditions.length],
    };
  });

  return { current, forecast };
}


export async function getWeather(
  prevState: WeatherState,
  formData: FormData
): Promise<WeatherState> {
  const schema = z.object({
    city: z.string().min(1, "City name cannot be empty."),
  });

  const validatedFields = schema.safeParse({
    city: formData.get("city"),
  });

  if (!validatedFields.success) {
    return {
      error: validatedFields.error.flatten().fieldErrors.city?.[0] ?? "Invalid city name.",
    };
  }

  const { city } = validatedFields.data;

  // Simulate network delay
  await new Promise((resolve) => setTimeout(resolve, 1000));
  
  // Simulate an API error for a specific city
  if (city.toLowerCase() === "error") {
    return {
      error: "Could not fetch weather data. The city was not found.",
    };
  }
  
  // Simulate saving last location (though it does nothing here)
  await new Promise((resolve) => setTimeout(resolve, 200));

  const weatherData = generateMockData(city);
  
  return {
    weatherData: weatherData,
    message: `Successfully fetched weather for ${weatherData.current.city}.`
  };
}

export async function rateForecast(rating: number, city: string) {
    // In a real app, you would store this rating in your database (e.g., Firebase)
    // associated with the city and perhaps the date of the forecast.
    console.log(`Rating for ${city}: ${rating} stars`);
    
    // Simulate network delay
    await new Promise((resolve) => setTimeout(resolve, 500));
    
    return { message: `Thank you for rating the forecast for ${city}!` };
}
