CREATE TABLE vehicle_classification (
    id UUID PRIMARY KEY,
    vehicleListingId VARCHAR(255) NOT NULL UNIQUE,
    isCategorized BOOLEAN NOT NULL DEFAULT FALSE,
    categorizedResult JSONB,
    createdAt TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updatedAt TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);
