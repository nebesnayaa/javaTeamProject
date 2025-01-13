import React from "react";

interface AppContextType {
    userId: string;
    setUserId: (userId: string) => void;
}

interface ContextProviderProps {
    children: React.ReactNode; // Correctly define the type of children
}

export const AppContext = React.createContext<AppContextType | null>(null);

export const ContextProvider: React.FC<ContextProviderProps> = ({ children }) => {
    const [userId, setUserId] = React.useState<string>("");

    const value: AppContextType = {
        userId,
        setUserId
    };

    return (
        <AppContext.Provider value={value}>
            {children}
        </AppContext.Provider>
    );
};
