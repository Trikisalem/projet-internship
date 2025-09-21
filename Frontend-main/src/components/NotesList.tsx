import { formatDistanceToNow } from "date-fns";
import { Badge } from "./ui/badge";
import { Button } from "./ui/button";
import { 
  MoreVerticalIcon, 
  TagIcon, 
  LockIcon, 
  ShareIcon, 
  GlobeIcon,
  TrashIcon,
  ExternalLinkIcon
} from "lucide-react";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "./ui/dropdown-menu";

interface Note {
  id: string;
  title: string;
  content: string;
  tags: string[];
  visibility: "PRIVATE" | "SHARED" | "PUBLIC";
  updatedAt: string;
  createdAt: string;
}

interface Props {
  notes: Note[];
  currentNote: Note | null;
  onSelectNote: (note: Note) => void;
  onDeleteNote: (noteId: string) => void;
}

export const NotesList = ({ notes, currentNote, onSelectNote, onDeleteNote }: Props) => {
  const getVisibilityIcon = (visibility: string) => {
    switch (visibility) {
      case "PRIVATE":
        return <LockIcon className="w-3 h-3" />;
      case "SHARED":
        return <ShareIcon className="w-3 h-3" />;
      case "PUBLIC":
        return <GlobeIcon className="w-3 h-3" />;
      default:
        return <LockIcon className="w-3 h-3" />;
    }
  };

  const getVisibilityColor = (visibility: string) => {
    switch (visibility) {
      case "PRIVATE":
        return "text-muted-foreground";
      case "SHARED":
        return "text-blue-500";
      case "PUBLIC":
        return "text-green-500";
      default:
        return "text-muted-foreground";
    }
  };

  const truncateContent = (content: string, maxLength = 100) => {
    // Remove markdown syntax for preview
    const plainText = content
      .replace(/#{1,6}\s+/g, '') // Remove headers
      .replace(/\*\*(.*?)\*\*/g, '$1') // Remove bold
      .replace(/\*(.*?)\*/g, '$1') // Remove italic
      .replace(/\[(.*?)\]\(.*?\)/g, '$1') // Remove links
      .replace(/`(.*?)`/g, '$1') // Remove inline code
      .replace(/\n/g, ' ') // Replace newlines with spaces
      .trim();
    
    if (plainText.length <= maxLength) return plainText;
    return plainText.substring(0, maxLength) + '...';
  };

  if (notes.length === 0) {
    return (
      <div className="flex-1 flex items-center justify-center p-8">
        <div className="text-center text-muted-foreground">
          <TagIcon className="w-12 h-12 mx-auto mb-4 opacity-50" />
          <p className="text-sm">No notes found</p>
          <p className="text-xs mt-1">Try adjusting your filters</p>
        </div>
      </div>
    );
  }

  return (
    <div className="flex-1 overflow-y-auto">
      <div className="p-2 space-y-1">
        {notes.map((note) => (
          <div
            key={note.id}
            className={`group relative p-3 rounded-lg cursor-pointer transition-all duration-200 ${
              currentNote?.id === note.id
                ? "bg-primary text-primary-foreground shadow-sm"
                : "hover:bg-note-hover"
            }`}
            onClick={() => onSelectNote(note)}
          >
            <div className="flex items-start justify-between mb-2">
              <div className="flex-1 min-w-0">
                <h3 className={`font-medium truncate ${
                  currentNote?.id === note.id ? "text-primary-foreground" : "text-foreground"
                }`}>
                  {note.title}
                </h3>
              </div>
              
              <div className="flex items-center space-x-1 ml-2 opacity-0 group-hover:opacity-100 transition-opacity">
                <div className={`${getVisibilityColor(note.visibility)} ${
                  currentNote?.id === note.id ? "text-primary-foreground/70" : ""
                }`}>
                  {getVisibilityIcon(note.visibility)}
                </div>
                
                <DropdownMenu>
                  <DropdownMenuTrigger asChild>
                    <Button
                      variant="ghost"
                      size="sm"
                      className={`h-6 w-6 p-0 ${
                        currentNote?.id === note.id 
                          ? "hover:bg-primary-foreground/20 text-primary-foreground/70" 
                          : "hover:bg-secondary"
                      }`}
                      onClick={(e) => e.stopPropagation()}
                    >
                      <MoreVerticalIcon className="w-3 h-3" />
                    </Button>
                  </DropdownMenuTrigger>
                  <DropdownMenuContent align="end" className="w-48">
                    <DropdownMenuItem>
                      <ExternalLinkIcon className="w-4 h-4 mr-2" />
                      Open in new tab
                    </DropdownMenuItem>
                    <DropdownMenuSeparator />
                    <DropdownMenuItem 
                      className="text-destructive focus:text-destructive"
                      onClick={(e) => {
                        e.stopPropagation();
                        onDeleteNote(note.id);
                      }}
                    >
                      <TrashIcon className="w-4 h-4 mr-2" />
                      Delete note
                    </DropdownMenuItem>
                  </DropdownMenuContent>
                </DropdownMenu>
              </div>
            </div>
            
            <p className={`text-sm mb-3 line-clamp-2 ${
              currentNote?.id === note.id ? "text-primary-foreground/80" : "text-muted-foreground"
            }`}>
              {truncateContent(note.content)}
            </p>
            
            {note.tags.length > 0 && (
              <div className="flex flex-wrap gap-1 mb-2">
                {note.tags.slice(0, 3).map((tag) => (
                  <Badge
                    key={tag}
                    variant="secondary"
                    className={`text-xs px-1.5 py-0.5 ${
                      currentNote?.id === note.id
                        ? "bg-primary-foreground/20 text-primary-foreground/80 hover:bg-primary-foreground/30"
                        : "bg-secondary/80 hover:bg-secondary"
                    }`}
                  >
                    <TagIcon className="w-2 h-2 mr-1" />
                    {tag}
                  </Badge>
                ))}
                {note.tags.length > 3 && (
                  <Badge
                    variant="secondary"
                    className={`text-xs px-1.5 py-0.5 ${
                      currentNote?.id === note.id
                        ? "bg-primary-foreground/20 text-primary-foreground/80"
                        : "bg-secondary/80"
                    }`}
                  >
                    +{note.tags.length - 3}
                  </Badge>
                )}
              </div>
            )}
            
            <div className={`text-xs ${
              currentNote?.id === note.id ? "text-primary-foreground/60" : "text-muted-foreground"
            }`}>
              Updated {formatDistanceToNow(new Date(note.updatedAt), { addSuffix: true })}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};