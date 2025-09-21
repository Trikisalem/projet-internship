import { useState } from "react";
import { Input } from "./ui/input";
import { Button } from "./ui/button";
import { Badge } from "./ui/badge";
import { Separator } from "./ui/separator";
import { 
  SearchIcon, 
  BookOpenIcon,
  LogOutIcon,
  FilterIcon,
  TagIcon,
  LockIcon,
  ShareIcon,
  GlobeIcon,
  XIcon
} from "lucide-react";

interface Props {
  onLogout: () => void;
  searchQuery: string;
  onSearchChange: (query: string) => void;
  selectedTags: string[];
  onTagsChange: (tags: string[]) => void;
  visibilityFilter: string;
  onVisibilityChange: (filter: string) => void;
  allTags: string[];
}

export const Sidebar = ({
  onLogout,
  searchQuery,
  onSearchChange,
  selectedTags,
  onTagsChange,
  visibilityFilter,
  onVisibilityChange,
  allTags
}: Props) => {
  const [showFilters, setShowFilters] = useState(false);

  const visibilityOptions = [
    { value: "all", label: "All Notes", icon: BookOpenIcon },
    { value: "private", label: "Private", icon: LockIcon },
    { value: "shared", label: "Shared", icon: ShareIcon },
    { value: "public", label: "Public", icon: GlobeIcon }
  ];

  const toggleTag = (tag: string) => {
    if (selectedTags.includes(tag)) {
      onTagsChange(selectedTags.filter(t => t !== tag));
    } else {
      onTagsChange([...selectedTags, tag]);
    }
  };

  const clearAllFilters = () => {
    onSearchChange("");
    onTagsChange([]);
    onVisibilityChange("all");
  };

  const hasActiveFilters = searchQuery || selectedTags.length > 0 || visibilityFilter !== "all";

  return (
    <div className="w-64 bg-sidebar border-r border-border flex flex-col">
      {/* Header */}
      <div className="p-4 border-b border-border">
        <div className="flex items-center space-x-3 mb-4">
          <div className="w-8 h-8 bg-gradient-primary rounded-lg flex items-center justify-center">
            <BookOpenIcon className="w-4 h-4 text-white" />
          </div>
          <h1 className="font-semibold text-lg">Notes Suite</h1>
        </div>
        
        {/* Search */}
        <div className="relative">
          <SearchIcon className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
          <Input
            placeholder="Search notes..."
            value={searchQuery}
            onChange={(e) => onSearchChange(e.target.value)}
            className="pl-10"
          />
        </div>
      </div>

      {/* Filters */}
      <div className="p-4 border-b border-border">
        <div className="flex items-center justify-between mb-3">
          <div className="flex items-center space-x-2">
            <FilterIcon className="w-4 h-4 text-muted-foreground" />
            <span className="text-sm font-medium">Filters</span>
          </div>
          <div className="flex items-center space-x-2">
            {hasActiveFilters && (
              <Button
                variant="ghost"
                size="sm"
                onClick={clearAllFilters}
                className="h-6 px-2 text-xs"
              >
                Clear
              </Button>
            )}
            <Button
              variant="ghost"
              size="sm"
              onClick={() => setShowFilters(!showFilters)}
              className="h-6 w-6 p-0"
            >
              {showFilters ? <XIcon className="w-3 h-3" /> : <FilterIcon className="w-3 h-3" />}
            </Button>
          </div>
        </div>

        {showFilters && (
          <div className="space-y-4">
            {/* Visibility Filter */}
            <div>
              <label className="text-xs font-medium text-muted-foreground mb-2 block">
                VISIBILITY
              </label>
              <div className="space-y-1">
                {visibilityOptions.map((option) => {
                  const Icon = option.icon;
                  return (
                    <button
                      key={option.value}
                      onClick={() => onVisibilityChange(option.value)}
                      className={`w-full flex items-center space-x-2 px-2 py-1.5 rounded text-sm transition-colors ${
                        visibilityFilter === option.value
                          ? "bg-primary text-primary-foreground"
                          : "hover:bg-note-hover"
                      }`}
                    >
                      <Icon className="w-3 h-3" />
                      <span>{option.label}</span>
                    </button>
                  );
                })}
              </div>
            </div>

            {/* Tags Filter */}
            {allTags.length > 0 && (
              <div>
                <label className="text-xs font-medium text-muted-foreground mb-2 block">
                  TAGS
                </label>
                <div className="flex flex-wrap gap-1">
                  {allTags.map((tag) => (
                    <Badge
                      key={tag}
                      variant={selectedTags.includes(tag) ? "default" : "secondary"}
                      className={`cursor-pointer text-xs ${
                        selectedTags.includes(tag)
                          ? "bg-primary hover:bg-primary/80"
                          : "hover:bg-secondary/80"
                      }`}
                      onClick={() => toggleTag(tag)}
                    >
                      <TagIcon className="w-2 h-2 mr-1" />
                      {tag}
                    </Badge>
                  ))}
                </div>
              </div>
            )}
          </div>
        )}
      </div>

      {/* Active Filters Display */}
      {hasActiveFilters && (
        <div className="p-4 border-b border-border">
          <div className="text-xs font-medium text-muted-foreground mb-2">ACTIVE FILTERS</div>
          <div className="space-y-2">
            {searchQuery && (
              <div className="flex items-center justify-between text-xs">
                <span>Search: "{searchQuery}"</span>
                <Button
                  variant="ghost"
                  size="sm"
                  onClick={() => onSearchChange("")}
                  className="h-4 w-4 p-0"
                >
                  <XIcon className="w-2 h-2" />
                </Button>
              </div>
            )}
            {visibilityFilter !== "all" && (
              <div className="flex items-center justify-between text-xs">
                <span>Visibility: {visibilityFilter}</span>
                <Button
                  variant="ghost"
                  size="sm"
                  onClick={() => onVisibilityChange("all")}
                  className="h-4 w-4 p-0"
                >
                  <XIcon className="w-2 h-2" />
                </Button>
              </div>
            )}
          </div>
        </div>
      )}

      {/* Spacer */}
      <div className="flex-1" />

      {/* User Actions */}
      <div className="p-4 border-t border-border">
        <Button
          variant="ghost"
          onClick={onLogout}
          className="w-full justify-start text-left"
        >
          <LogOutIcon className="w-4 h-4 mr-2" />
          Sign Out
        </Button>
      </div>
    </div>
  );
};